package com.lmaguado.hulkStore.models.implement.dto.database;

import com.lmaguado.hulkStore.helpers.strings.DatabaseStrings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetTokenBlacklistDTO {
    private final String token;
    private final String own;
    private final String reason;
    private final Integer blockedBy;
    private final Date expired;

    public GetTokenBlacklistDTO(String token, String own, String reason, Integer blockedBy, Date expired) {
        this.token = token;
        this.own = own;
        this.reason = reason;
        this.blockedBy = blockedBy;
        this.expired = expired;
    }

    public String getToken() {
        return token;
    }

    public String getOwn() {
        return own;
    }

    public String getReason() {
        return reason;
    }

    public Integer getBlockedBy() {
        return blockedBy;
    }

    public Date getExpired() {
        return expired;
    }

    @Override
    public String toString() {
        return "GetTokenBlacklistDTO{" +
                "token='" + token + '\'' +
                ", own='" + own + '\'' +
                ", reason='" + reason + '\'' +
                ", blockedBy='" + blockedBy + '\'' +
                ", expired=" + expired +
                '}';
    }

    public static List<GetTokenBlacklistDTO> getList(ResultSet result) throws SQLException {
        List<GetTokenBlacklistDTO> list = new ArrayList<>();
        while (result.next()) {
            GetTokenBlacklistDTO item = new GetTokenBlacklistDTO(
                    result.getString(DatabaseStrings.GET_TOKEN_BLACKLIST_TOKEN),
                    result.getString(DatabaseStrings.GET_TOKEN_BLACKLIST_OWN),
                    result.getString(DatabaseStrings.GET_TOKEN_BLACKLIST_REASON),
                    result.getInt(DatabaseStrings.GET_TOKEN_BLACKLIST_BLOCKED),
                    result.getDate(DatabaseStrings.GET_TOKEN_BLACKLIST_EXPIRED)
            );
            list.add(item);
        }
        return list;
    }
}
