package com.lmaguado.hulkStore.models.implement.dto.database;

import com.lmaguado.hulkStore.helpers.strings.DatabaseStrings;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetUserDTO {
    private final long userId;
    private final String userName;
    private final String userEmail;
    private final String permLabel;
    private final int permWeight;

    public GetUserDTO(long userId, String userName, String userEmail, String permLabel, int permWeight) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.permLabel = permLabel;
        this.permWeight = permWeight;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPermLabel() {
        return permLabel;
    }

    public int getPermWeight() {
        return permWeight;
    }

    @Override
    public String toString() {
        return "GetUserDTO{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", permLabel='" + permLabel + '\'' +
                ", permWeight=" + permWeight +
                '}';
    }

    public static List<GetUserDTO> getList(ResultSet result) throws SQLException {
        List<GetUserDTO> list = new ArrayList<>();
        while (result.next()) {
            GetUserDTO item = new GetUserDTO(
                    result.getLong(DatabaseStrings.GET_USER_ID),
                    result.getString(DatabaseStrings.GET_USER_NAME),
                    result.getString(DatabaseStrings.GET_USER_EMAIL),
                    result.getString(DatabaseStrings.GET_USER_PERM_LABEL),
                    result.getInt(DatabaseStrings.GET_USER_PERM_WEIGHT)
            );
            list.add(item);
        }
        return list;
    }
}
