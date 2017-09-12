package com.tencent.dingdang.tvs.http;

import com.tencent.dingdang.tvs.AudioInputFormat;

import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.util.InputStreamContentProvider;

import java.io.InputStream;

/**
 * A {@link ContentProvider} that streams an InputStream in chunks with size provided by
 * {@link AudioInputFormat#getChunkSizeBytes()}.
 */
public class AudioInputStreamContentProvider extends InputStreamContentProvider implements
        ContentProvider.Typed {

    public AudioInputStreamContentProvider(AudioInputFormat audioType, InputStream stream) {
        super(stream, audioType.getChunkSizeBytes());
    }

    @Override
    public String getContentType() {
        return ContentTypes.AUDIO;
    }
}
