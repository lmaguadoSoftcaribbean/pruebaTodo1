package com.lmaguado.hulkStore.models.implement.dto.database;

import com.lmaguado.hulkStore.helpers.strings.DatabaseStrings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetProductDTO {
    private String code;
    private String name;
    private Integer unit;

    public GetProductDTO(String code, String name, Integer unit) {
        this.code = code;
        this.name = name;
        this.unit = unit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUnit() {
        return unit != null ? unit : 0;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public static List<GetProductDTO> getList(ResultSet resultSet) throws SQLException {
        List<GetProductDTO> list = new ArrayList<>();
        while (resultSet.next()) {
            GetProductDTO item = new GetProductDTO(
                    resultSet.getString(DatabaseStrings.GET_PRODUCTOS_CODE),
                    resultSet.getString(DatabaseStrings.GET_PRODUCTOS_NAME),
                    resultSet.getInt(DatabaseStrings.GET_PRODUCTOS_UNIT)
            );
            list.add(item);
        }
        return list;
    }
}
