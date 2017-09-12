package com.tencent.dingdang.tvs.message.response;

import com.tencent.dingdang.tvs.message.Payload;

import java.io.InputStream;

/**
 * Specify a type of {@link Payload} that references attached audio content via a Content-ID HTTP
 * Header value.
 */
public interface AttachedContentPayload {

    /**
     * Returns whether or not this payload requires content to be attached. False means either it
     * never required content, or that it has content.
     */
    boolean requiresAttachedContent();

    /**
     * Returns whether or not this payload has content attached.
     */
    boolean hasAttachedContent();

    /**
     * Returns the content id for the required attached content.
     */
    String getAttachedContentId();

    /**
     * Returns the attached content.
     */
    InputStream getAttachedContent();

    /**
     * Attaches the given attachment content if the given content id matches the required content
     * id.
     *
     * @param contentId
     *            - content id of attachementContent
     * @param attachmentContent
     *            - content to attach
     */
    void setAttachedContent(String contentId, InputStream attachmentContent);
}
