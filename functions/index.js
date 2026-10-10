const { onRequest } = require("firebase-functions/v2/https");
const { defineSecret } = require("firebase-functions/params");
const express = require("express");
const axios = require("axios");

const comicsVineApiKey = defineSecret("COMICS_VINE_API_KEY");
const app = express();

app.get("*", async (req, res) => {
  try {
    const subpath = req.path.replace(/^\/+/, "");
    const targetUrl = `https://comicvine.gamespot.com/api/${subpath}`;

    console.log("Proxying request to:", targetUrl, "Query:", req.query);

    const response = await axios.get(targetUrl, {
      params: {
        ...req.query,
        api_key: comicsVineApiKey.value(),
        format: "json",
      },
      headers: {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
        "Referer": "https://comicvine.gamespot.com/",
        "Accept": "application/json",
      },
    });

    res.status(response.status).json(response.data);
  } catch (error) {
    console.error("Proxy error:", error.message, error.response?.data);
    res.status(error.response?.status || 500).json({
      error: error.message,
      details: error.response?.data || null,
    });
  }
});

exports.comicvineproxy = onRequest({ secrets: [comicsVineApiKey], invoker: "public" }, app);
