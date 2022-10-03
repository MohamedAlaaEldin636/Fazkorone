package com.grand.ezkorone.core.extensions

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.media.audiofx.Equalizer
import android.net.Uri
import android.os.Handler
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioRendererEventListener
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil
import com.google.android.exoplayer2.metadata.MetadataOutput
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.text.TextOutput
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.video.VideoRendererEventListener
import timber.log.Timber
import com.grand.ezkorone.R

fun Context.createMediaPlayerInitializeAndPlay(url: String) {
    MediaPlayer().abc(this, url)
}

private fun MediaPlayer.abc(appContext: Context, url: String) {
    //setAudioStreamType(AudioManager.STREAM_MUSIC)
    kotlin.runCatching {
        Timber.e("errorrrrr ch 0")
        setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .build()
        )
        Timber.e("errorrrrr ch 0.5 $url")
        //Uri url = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.usa_for_africa_we_are_the_world);
        setDataSource(url/*appContext, Uri.parse(url)*//*Uri.parse("android.resource://${appContext.packageName}/${R.raw.sms}")*/)
        Timber.e("errorrrrr ch 1")
        prepare/*Async*/()
        Timber.e("errorrrrr ch 2")
        start()
        Timber.e("errorrrrr ch 4")
    }.getOrElse {
        Timber.e("errorrrrr is $it")
    }
}

fun ExoPlayer.Builder.performSetups(appContext: Context) = apply {
    val defRendererFactory = DefaultRenderersFactory(appContext)
    defRendererFactory.setMediaCodecSelector { mimeType, requiresSecureDecoder, requiresTunnelingDecoder ->
        listOf(
            MediaCodecInfo.newInstance(
                "OMX.google.raw.decoder",
                "audio/raw",
                "audio/raw",
                //null,
                //"OMX.MTK.AUDIO.DECODER.RAW",//"OMX.google.raw.decoder",
                //MimeTypes.AUDIO_RAW,
                //mimeType,
                null,
                true,
                false,
                false,
                false,
                true
            )
        ).also {
            Timber.e("list 1 -> $it")
        }/*.plus(
            MediaCodecUtil.getDecoderInfos(mimeType, requiresSecureDecoder, requiresTunnelingDecoder).also {
                Timber.e("list 2 -> $it")
            }
        )*/

        //listOf()
    }
    setRenderersFactory(defRendererFactory)
    //setMediaSourceFactory(DefaultMediaSourceFactory(appContext))
}

// tozdo perform if wav isa.
fun ExoPlayer.Builder.enableEqualizer(appContext: Context): ExoPlayer.Builder = apply {
    //Equalizer(1, audioSessionId).enabled = true

    Timber.e("hello hhhhhhhhhhhh")

    MediaCodecUtil.getDecoderInfos("wav", true, true).also {
        Timber.e("hello 1 $it")
    }
    MediaCodecUtil.getDecoderInfos("wav", true, false).also {
        Timber.e("hello 2 $it")
    }
    MediaCodecUtil.getDecoderInfos("wav", false, true).also {
        Timber.e("hello 3 $it")
    }
    MediaCodecUtil.getDecoderInfos("wav", false, false).also {
        Timber.e("hello 4 $it")
    }

    /*object : RenderersFactory {
        override fun createRenderers(
            eventHandler: Handler,
            videoRendererEventListener: VideoRendererEventListener,
            audioRendererEventListener: AudioRendererEventListener,
            textRendererOutput: TextOutput,
            metadataRendererOutput: MetadataOutput
        ): Array<Renderer> {
            TODO("Not yet implemented")
        }
    }*/
    val defFactory = DefaultRenderersFactory(appContext)
    defFactory.setMediaCodecSelector { mimeType, requiresSecureDecoder, requiresTunnelingDecoder ->
        MediaCodecUtil.getDecoderInfos(mimeType, requiresSecureDecoder, requiresTunnelingDecoder).also {
            Timber.e("hello $it")
        }
    }

    setRenderersFactory(defFactory)



    //DefaultRenderersFactory(appContext)
    /*val renderersFactory = DefaultRenderersFactory(appContext)
    renderersFactory.setMediaCodecSelector(object : MediaCodecSelector {
        override fun getDecoderInfos(
            mimeType: String,
            requiresSecureDecoder: Boolean,
            requiresTunnelingDecoder: Boolean
        ): MutableList<MediaCodecInfo> {
            if (mimeType == MimeTypes.AUDIO_RAW) {
            }else {

            }

            TODO("Not yet implemented")
        }
    })*/
}
/*
    public ExoMediaPlayer(Context context) {
        mContext = context.getApplicationContext();
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(context);
        if (isMTKDevice()) {
            renderersFactory.setMediaCodecSelector(new MediaCodecSelector() {
                @Override
                public List<MediaCodecInfo> getDecoderInfos(String mimeType, boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
                    List<MediaCodecInfo> decoderInfos;
                    if (mimeType.equals(MimeTypes.AUDIO_RAW)) {
                        decoderInfos = new ArrayList<>(1);
                        MediaCodecInfo mediaCodecInfo = MediaCodecInfo.newInstance("OMX.google.raw.decoder", MimeTypes.AUDIO_RAW, null);
                        decoderInfos.add(mediaCodecInfo);
                    } else {
                        decoderInfos = MediaCodecUtil.getDecoderInfos(mimeType, requiresSecureDecoder);
                    }
                    return decoderInfos.isEmpty()
                            ? Collections.emptyList()
                            : Collections.singletonList(decoderInfos.get(0));
                }

                @Nullable
                @Override
                public MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
                    return MediaCodecUtil.getPassthroughDecoderInfo();
                }
            });
        }
        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context);
        mSimpleExoPlayer.addListener(new MediaEventListener());
    }

    // MediaCodecUtil#isCodecUsableDecoder not handle it, so we do it
    private boolean isMTKDevice() {
        try {
            MediaCodecInfo decoderInfo = MediaCodecUtil.getDecoderInfo(MimeTypes.AUDIO_RAW, false);
            Log.i(TAG, "decoderInfo " + decoderInfo);
            if (decoderInfo != null) {
                return "OMX.MTK.AUDIO.DECODER.RAW".equals(decoderInfo.name);
            }
        } catch (MediaCodecUtil.DecoderQueryException e) {
            Log.e(TAG, "isMTKDevice: ", e);
        }
        return false;
    }
 */
