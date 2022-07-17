package ru.loviagin.tapscrolling.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.loviagin.tapscrolling.R;
import ru.loviagin.tapscrolling.activities.MainActivity;
import ru.loviagin.tapscrolling.activities.AuthorizeActivity;
import ru.loviagin.tapscrolling.objects.Comment;
import ru.loviagin.tapscrolling.objects.Video;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<Video> videos;
    private ProgressBar progressBar;
    private Context context;
    // private Video video;
    //  private OnVideoClickListener onVideoClickListener;

//    public void setOnVideoClickListener(OnVideoClickListener onVideoClickListener) {
//        this.onVideoClickListener = onVideoClickListener;
//    }

    public VideoAdapter(ProgressBar progressBar, Context context) {
        videos = new ArrayList<>();
        this.progressBar = progressBar;
        this.context = context;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public void addVideo(Video video) {
        videos.add(video);
        //notifyItemInserted(getItemCount() - 1);
    }

    public void addVideo(
            int videoId,
            String video_url,
            int video_id,
            int user_id,
            int replies_count,
            int likes_count,
            int video_views,
            List<Comment> comments_array,
            String avatar_url

    ) {
        videos.add(new Video(video_url, video_id, user_id, replies_count, likes_count, video_views, comments_array, avatar_url));
        notifyItemInserted(getItemCount() - 1);
    }
    // public interface OnVideoClickListener {
    //         void onLikeClick();
//
//          void onCommentClick();
//
//          void onReplyClick();

    //  }

//    public Video getVideo() {
//        return video;
//    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videos.get(position);
        setLikes(video, holder);
        holder.textViewRepliesCount.setText(String.valueOf(video.getReplies_count()));

        try {
            Picasso.get().load(Uri.parse(video.getAvatar_url())).placeholder(R.drawable.user).into(holder.imageView);
        } catch (Exception e) {
            Log.i("App exc", "Err load img");
        }

//        holder.textViewCommentsCount.setText(String.valueOf(video.getComments_array().get(position).getCountOfComments()));


        try {
            holder.videoView.setVideoPath(video.getVideo_url() + ".mp4");
            holder.videoView.setOnCompletionListener(mp -> {
                video.addVideoView();
                holder.videoView.start();
            });
            progressBar.setVisibility(View.INVISIBLE);
            holder.videoView.start();
        } catch (Exception e) {
            Toast.makeText(holder.itemView.getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
        if (MainActivity.isRegistered()) {
            holder.imageButtonLike.setOnClickListener(v -> {
                video.addLike(holder.imageButtonLike);
                setLikes(video, holder);
                // holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.shake));
            });
        } else {
            holder.imageButtonLike.setOnClickListener(view -> context.startActivity(new Intent(context, AuthorizeActivity.class)));
            holder.imageButtonComment.setOnClickListener(view -> context.startActivity(new Intent(context, AuthorizeActivity.class)));
            holder.imageButtonReply.setOnClickListener(view -> context.startActivity(new Intent(context, AuthorizeActivity.class)));
        }

    }

    private void setLikes(Video video, VideoViewHolder holder){
        int likes_count = video.getLikes_count();
        if (likes_count >= 1000 && likes_count < 1000000) {
            double count = (double) likes_count / 1000;
            if (likes_count % 1000 == 0) {
                holder.textViewLikesCount.setText(String.format("%sК", (int) count));
            } else {
                holder.textViewLikesCount.setText(String.format("%sК", count));
            }

        } else if (likes_count >= 1000000) {
            double count = (double) likes_count / 1000000;
            holder.textViewLikesCount.setText(String.format("%sМ", count));
        } else {
            holder.textViewLikesCount.setText(String.valueOf(likes_count));
        }
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private final VideoView videoView;
        boolean isStart = true;
        private TextView textViewLikesCount;
        private TextView textViewCommentsCount;
        private TextView textViewRepliesCount;
        private ImageButton imageButtonLike;
        private ImageButton imageButtonComment;
        private ImageButton imageButtonReply;
        private ImageView imageView;

        @SuppressLint("ClickableViewAccessibility")
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            textViewCommentsCount = itemView.findViewById(R.id.textViewCommentsCount);
            textViewLikesCount = itemView.findViewById(R.id.textViewLikesCount);
            textViewRepliesCount = itemView.findViewById(R.id.textViewRepliesCount);
            imageButtonComment = itemView.findViewById(R.id.imageButtonComment);
            imageButtonLike = itemView.findViewById(R.id.imageButtonLike);
            imageButtonReply = itemView.findViewById(R.id.imageButtonReply);
            imageView = itemView.findViewById(R.id.imageViewAvatar);

            videoView.setOnTouchListener((v, event) -> {
                if (isStart) {
                    videoView.pause();
                    isStart = false;
                } else {
                    videoView.start();
                    isStart = true;
                }
                return false;
            });
        }
    }

}
