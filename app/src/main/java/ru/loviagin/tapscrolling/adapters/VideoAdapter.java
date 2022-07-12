package ru.loviagin.tapscrolling.adapters;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.loviagin.tapscrolling.R;
import ru.loviagin.tapscrolling.data.Comment;
import ru.loviagin.tapscrolling.data.Video;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<Video> videos;
   // private Video video;
    //  private OnVideoClickListener onVideoClickListener;

//    public void setOnVideoClickListener(OnVideoClickListener onVideoClickListener) {
//        this.onVideoClickListener = onVideoClickListener;
//    }

    public VideoAdapter() {
        videos = new ArrayList<>();
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
            List<Comment> comments_array
            ) {
        videos.add(new Video(video_url, video_id, user_id, replies_count, likes_count, video_views, comments_array));
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
        holder.textViewRepliesCount.setText(String.valueOf(video.getReplies_count()));
        holder.imageButtonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.addLike(holder.imageButtonLike);
                // holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.shake));
            }
        });

//        holder.textViewCommentsCount.setText(String.valueOf(video.getComments_array().get(position).getCountOfComments()));


        try {
            holder.videoView.setVideoPath(video.getVideo_url() + ".mp4");
            holder.videoView.setOnCompletionListener(mp -> {
                video.addVideoView();
                holder.videoView.start();
            });

            holder.videoView.start();
        } catch (Exception e) {
            Toast.makeText(holder.itemView.getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
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
        //        private TextView textViewCommentsCount;
        private TextView textViewRepliesCount;
        private ImageButton imageButtonLike;
//        private ImageButton imageButtonComment;
//        private ImageButton imageButtonReply;

        @SuppressLint("ClickableViewAccessibility")
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
//            textViewCommentsCount = itemView.findViewById(R.id.textViewCommentsCount);
            textViewLikesCount = itemView.findViewById(R.id.textViewLikesCount);
            textViewRepliesCount = itemView.findViewById(R.id.textViewRepliesCount);
//            imageButtonComment = imageButtonLike.findViewById(R.id.imageButtonComment);
            imageButtonLike = itemView.findViewById(R.id.imageButtonLike);
//            imageButtonReply = itemView.findViewById(R.id.imageButtonReply);

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


           /* imageButtonLike.setOnClickListener(v -> {
                if (onVideoClickListener != null) {
                    onVideoClickListener.onLikeClick();
                }
            });


            imageButtonComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onVideoClickListener != null) {
                        onVideoClickListener.onCommentClick();
                    }
                }
            });

            imageButtonReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onVideoClickListener != null) {
                        onVideoClickListener.onReplyClick();
                    }
                }
            });*/
        }
    }

}
