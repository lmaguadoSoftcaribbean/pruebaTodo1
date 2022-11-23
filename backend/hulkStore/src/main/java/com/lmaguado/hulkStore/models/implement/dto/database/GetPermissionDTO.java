package com.lmaguado.hulkStore.models.implement.dto.database;

import com.lmaguado.hulkStore.helpers.strings.DatabaseStrings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetPermissionDTO {
    private final int permitId;
    private final String permitLabel;
    private final int permitWeight;

    public GetPermissionDTO(int permitId, String permitLabel, int permitWeight) {
        this.permitId = permitId;
        this.permitLabel = permitLabel;
        this.permitWeight = permitWeight;
    }

    public int getPermitId() {
        return permitId;
    }

    public String getPermitLabel() {
        return permitLabel;
    }

    public int getPermitWeight() {
        return permitWeight;
    }

    @Override
    public String toString() {
        return "GetPermissionDTO{" +
                "permitId=" + permitId +
                ", permitLabel='" + permitLabel + '\'' +
                ", permitWeight=" + permitWeight +
                '}';
    }

    public static List<GetPermissionDTO> getList(ResultSet result) throws SQLException {
        List<GetPermissionDTO> list = new ArrayList<>();
        while (result.next()) {
            GetPermissionDTO item = new GetPermissionDTO(
                    result.getInt(DatabaseStrings.GET_PERMISSION_ID),
                    result.getString(DatabaseStrings.GET_PERMISSION_LABEL),
                    result.getInt(DatabaseStrings.GET_PERMISSION_WEIGHT)
            );
            list.add(item);
        }
        return list;
    }
}
