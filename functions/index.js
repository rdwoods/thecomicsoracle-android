const { onRequest } = require("firebase-functions/v2/https");
const { defineSecret } = require("firebase-functions/v2/params");
const axios = require("axios");

const comicsVineApiKey = defineSecret("ComicsVineApiKey");

exports.comicvineproxy = onRequest({ secrets: [comicsVineApiKey] }, async (req, res) => {
  try {
    const subpath = req.path.replace(/^\/+/, "");
    const targetUrl = `https://comicvine.gamespot.com/api/${subpath}`;

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
    console.error("Proxy error:", error.message);
    res.status(error.response?.status || 500).json({
      error: error.message,
      details: error.response?.data || null,
    });
  }
});
