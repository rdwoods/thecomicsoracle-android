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
import com.rwoods.thecomicsoracle.entity.ComicCharacter;
import com.rwoods.thecomicsoracle.entity.Image;

import java.util.ArrayList;

/**
 * Created by rahmanwoods on 5/12/15.
 */
public class ComicCharacterAdapter extends RecyclerView.Adapter<ComicCharacterAdapter.CharacterViewHolder> {

    private final Context mContext;
    private ArrayList<ComicCharacter> mComicCharacterList;
    private ArrayList<ComicCharacter> mComicCharacterSearchList;
    private OnItemClickListener mItemClickListener;


    public ComicCharacterAdapter(Context context, ArrayList<ComicCharacter> comicCharacterList) {
        this.mContext = context;
        this.mComicCharacterList = comicCharacterList;
        this.mComicCharacterSearchList = new ArrayList<>();
        this.mComicCharacterSearchList.addAll(mComicCharacterList);
    }

    public void add(ComicCharacter e, int position) {
        position = position == -1 ? getItemCount() : position;
        mComicCharacterSearchList.add(position, e);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            mComicCharacterSearchList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ArrayList<ComicCharacter> getSearchedCharacterList() {
        return mComicCharacterSearchList;
    }


    class CharacterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivCharacterThumbnail = null;
        final TextView tvCharacterName;

        CharacterViewHolder(View view) {
            super(view);
            ivCharacterThumbnail = (ImageView) view.findViewById(R.id.character_thumbnail);
            tvCharacterName = (TextView) view.findViewById(R.id.character_name);
            tvCharacterName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, (Integer) tvCharacterName.getTag());
            }
        }
    }

    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.character_listrow, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, final int position) {
        Image image = mComicCharacterSearchList.get(position).getImage();
        String characterName = mComicCharacterSearchList.get(position).getName();
        String gender = mComicCharacterSearchList.get(position).getGender();
        String thumbUrl = null;

        int placeholderImageId;
        int errorImageId;

        if (characterName != null ){
            holder.tvCharacterName.setTag(position);
            holder.tvCharacterName.setText(characterName);
        }

        if (gender.contentEquals("1")){
            placeholderImageId = R.drawable.tco_placeholder_male;
            errorImageId = R.drawable.tco_placeholder_male;
        } else {
            placeholderImageId = R.drawable.tco_placeholder_female;
            errorImageId = R.drawable.tco_placeholder_female;
        }

        if (image != null) {
            String url = image.getIconUrl();

            if (url != null) {
                Glide.with(mContext)
                        .load(url)
                        .placeholder(R.drawable.default_profile_avatar)
                        .error(R.drawable.default_profile_avatar)
                        .centerCrop()
                        .into(holder.ivCharacterThumbnail);

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
        return mComicCharacterSearchList.size();
    }

    public ArrayList<ComicCharacter> getCharacterList() {
        return mComicCharacterList;
    }
}