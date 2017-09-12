package com.tencent.dingdang.tvs;

import com.tencent.dingdang.tvs.message.response.Directive;

public interface DirectiveDispatcher {
    void dispatch(Directive directive);
}
