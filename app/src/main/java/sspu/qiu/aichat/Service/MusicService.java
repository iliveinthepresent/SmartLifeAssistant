package sspu.qiu.aichat.Service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    public MediaPlayer mediaPlayer;

    public class MyBinder extends Binder {
        // 播放音乐
        public void play(String path) {
            try {
                if (mediaPlayer == null) {
                    // 创建一个MediaPlayer播放器
                    mediaPlayer = new MediaPlayer();
                     // 指定参数为音频文件
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                     // 指定播放的路径
                    mediaPlayer.setDataSource(path);
                     // 准备播放
                    mediaPlayer.prepare();
                    mediaPlayer.setOnPreparedListener(new
                           MediaPlayer.OnPreparedListener() {
                        public void onPrepared (MediaPlayer mp)
                        {
                            // 开始播放
                            mediaPlayer.start();
                            }
                        });
                    } else {
                    int position = getCurrentProgress();
                    mediaPlayer.seekTo(position);
                    try {
                        mediaPlayer.prepare();
                        } catch (Exception e) {
                        e.printStackTrace();
                        }
                    mediaPlayer.start();
                    }
                } catch (Exception e) {
                e.printStackTrace();
                }
            }
        // 暂停播放
        public void pause() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause(); // 暂停播放
                } else if (mediaPlayer != null && (!mediaPlayer.isPlaying())) {
                mediaPlayer.start();
                }
            }

    }


    public void onCreate() {
        super.onCreate();
        }


    // 获取当前进度
    public int getCurrentProgress() {
        if (mediaPlayer != null & mediaPlayer.isPlaying()) {
            return mediaPlayer.getCurrentPosition();
            } else if (mediaPlayer != null & (!mediaPlayer.isPlaying())) {
            return mediaPlayer.getCurrentPosition();
            }
        return 0;
        }


    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            }
        super.onDestroy();
        }


    @Override
    public IBinder onBind(Intent intent) {
        // 第一步执行onBind方法
        return new MyBinder();
        }
}