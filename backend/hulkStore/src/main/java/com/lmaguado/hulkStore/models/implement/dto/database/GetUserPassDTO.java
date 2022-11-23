package com.lmaguado.hulkStore.models.implement.dto.database;

import com.lmaguado.hulkStore.helpers.strings.DatabaseStrings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetUserPassDTO {
    private final String userPassword;

    public GetUserPassDTO(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPassword() {
        return userPassword;
    }

    @Override
    public String toString() {
        return "GetUserPassDTO{" +
                "userPassword='" + userPassword + '\'' +
                '}';
    }

    public static List<GetUserPassDTO> getList(ResultSet result) throws SQLException {
        List<GetUserPassDTO> list = new ArrayList<>();
        while (result.next()) {
            GetUserPassDTO item = new GetUserPassDTO(
                    result.getString(DatabaseStrings.GET_USER_PASS_PASSWORD)
            );
            list.add(item);
        }
        return list;
    }
}
