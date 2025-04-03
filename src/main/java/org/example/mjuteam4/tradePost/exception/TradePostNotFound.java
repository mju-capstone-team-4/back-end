package org.example.mjuteam4.tradePost.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class TradePostNotFound extends GlobalException {

    public TradePostNotFound() {
        super(ExceptionCode.TRADE_POST_NOT_FOUND);
    }
}
