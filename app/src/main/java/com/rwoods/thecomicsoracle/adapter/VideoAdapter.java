package com.rwoods.thecomicsoracle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.rwoods.thecomicsoracle.R;
import com.rwoods.thecomicsoracle.entity.Image;
import com.rwoods.thecomicsoracle.entity.Video;

import java.util.ArrayList;

/**
 * Created by rahmanwoods on 5/12/15.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private final Context mContext;
    private ArrayList<Video> mVideoList;
    private ArrayList<Video> mVideoSearchList;
    private OnItemClickListener mItemClickListener;


    public VideoAdapter(Context context, ArrayList<Video> videoList) {
        this.mContext = context;
        this.mVideoList = videoList;
        this.mVideoSearchList = new ArrayList<>();
        this.mVideoSearchList.addAll(mVideoList);
    }

    public void add(Video e, int position) {
        position = position == -1 ? getItemCount() : position;
        mVideoSearchList.add(position, e);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            mVideoSearchList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ArrayList<Video> getSearchedVideoList() {
        return mVideoSearchList;
    }


    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivVideoThumbnail = null;
        final TextView tvVideoTitle;

        VideoViewHolder(View view) {
            super(view);
            ivVideoThumbnail = (ImageView) view.findViewById(R.id.video_thumbnail);
            tvVideoTitle = (TextView) view.findViewById(R.id.video_name);
            tvVideoTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, (Integer) tvVideoTitle.getTag());
            }
        }
    }

    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.video_listrow, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, final int position) {
        Image image = mVideoSearchList.get(position).getImage();
        String characterName = mVideoSearchList.get(position).getName();

        if (characterName != null ){
            holder.tvVideoTitle.setTag(position);
            holder.tvVideoTitle.setText(characterName);
        }

        if (image != null) {
            String url = image.getIconUrl();

            if (url != null) {
                Glide.with(mContext)
                        .load(url)
                        .placeholder(R.drawable.default_profile_avatar)
                        .error(R.drawable.default_profile_avatar)
                        .centerCrop()
                        .into(holder.ivVideoThumbnail);

            }
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    @Override
    public int getItemCount() {
        return mVideoSearchList.size();
    }

    public ArrayList<Video> getVideoList() {
        return mVideoList;
    }
}