package com.lmaguado.hulkStore.services;

import com.lmaguado.hulkStore.helpers.strings.DatabaseStrings;
import com.lmaguado.hulkStore.models.implement.ConnectionPoolImp;
import com.lmaguado.hulkStore.models.implement.dto.database.GetPermissionDTO;
import com.lmaguado.hulkStore.models.implement.dto.database.GetTokenBlacklistDTO;
import com.lmaguado.hulkStore.models.implement.dto.database.GetUserDTO;
import com.lmaguado.hulkStore.models.implement.dto.database.GetUserPassDTO;
import com.lmaguado.hulkStore.models.implement.exceptions.ConnectionPoolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class QueriesServices {

    private final ConnectionPoolImp connectionPool;
    private final Logger logger = LoggerFactory.getLogger(QueriesServices.class);

    @Autowired
    public QueriesServices(ConnectionPoolImp connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * ===========================================================
     * TABLE crl_params
     * ===========================================================
     */
    public String getGlobalParam(String key) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String rtn = null;
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return null;
            preparedStatement = conn.prepareStatement("CALL " + DatabaseStrings.GET_DATABASE_NAME + ".GET_PARAM(?);");
            preparedStatement.setString(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) rtn = resultSet.getString("PARAM_VALUE");
        } catch (SQLException | ConnectionPoolException e) {
            printError(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                printError(e);
            }
            connectionPool.releaseConnection(conn);
        }
        return rtn;
    }

    /**
     * ===========================================================
     * TABLE grl_users
     * ===========================================================
     */
    public long isUserExists(String username) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        long userExits = -1;
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return userExits;
            preparedStatement = conn.prepareStatement("CALL " + DatabaseStrings.GET_DATABASE_NAME + ".IS_USER_EXISTS(?);");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) userExits = resultSet.getInt(DatabaseStrings.GET_RESULT);
        } catch (SQLException | ConnectionPoolException e) {
            printError(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                printError(e);
            }
            connectionPool.releaseConnection(conn);
        }
        return userExits;
    }

    public long createUser(String username, String password, String email) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        long userId = -1;
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return userId;
            preparedStatement = conn.prepareStatement("CALL " + DatabaseStrings.GET_DATABASE_NAME + ".CREATE_USER(?, ?, ?);");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) userId = resultSet.getLong(DatabaseStrings.GET_USER_ID);
        } catch (SQLException | ConnectionPoolException e) {
            printError(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                printError(e);
            }
            connectionPool.releaseConnection(conn);
        }
        return userId;
    }

    public List<GetUserDTO> getUserByUserName(String username) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        List<GetUserDTO> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return list;
            preparedStatement = conn.prepareStatement("CALL " + DatabaseStrings.GET_DATABASE_NAME + ".GET_USER_BY_USERNAME(?);");
            preparedStatement.setString(1, username);
            list = GetUserDTO.getList(preparedStatement.executeQuery());
        } catch (SQLException | ConnectionPoolException e) {
            printError(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                printError(e);
            }
            connectionPool.releaseConnection(conn);
        }
        return list;
    }

    public List<GetUserDTO> getUserByUserId(long userId) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        List<GetUserDTO> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return list;
            preparedStatement = conn.prepareStatement("CALL " + DatabaseStrings.GET_DATABASE_NAME + ".GET_USER_BY_ID(?);");
            preparedStatement.setLong(1, userId);
            list = GetUserDTO.getList(preparedStatement.executeQuery());
        } catch (SQLException | ConnectionPoolException e) {
            printError(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                printError(e);
            }
            connectionPool.releaseConnection(conn);
        }
        return list;
    }

    public List<GetUserPassDTO> getUserPass(long userId) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        List<GetUserPassDTO> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return list;
            preparedStatement = conn.prepareStatement("CALL " + DatabaseStrings.GET_DATABASE_NAME + ".GET_USER_PASS(?);");
            preparedStatement.setLong(1, userId);
            list = GetUserPassDTO.getList(preparedStatement.executeQuery());
        } catch (SQLException | ConnectionPoolException e) {
            printError(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                printError(e);
            }
            connectionPool.releaseConnection(conn);
        }
        return list;
    }


    public List<GetPermissionDTO> getPermissions() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        List<GetPermissionDTO> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return list;
            preparedStatement = conn.prepareStatement("CALL " + DatabaseStrings.GET_DATABASE_NAME + ".GET_PERMISSIONS();");
            list = GetPermissionDTO.getList(preparedStatement.executeQuery());
        } catch (SQLException | ConnectionPoolException e) {
            printError(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                printError(e);
            }
            connectionPool.releaseConnection(conn);
        }
        return list;
    }

    /**
     * ===========================================================
     * TABLE grl_permissions
     * ===========================================================
     */
    public List<GetPermissionDTO> getPermission(int permitId) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        List<GetPermissionDTO> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return list;
            preparedStatement = conn.prepareStatement("CALL " + DatabaseStrings.GET_DATABASE_NAME + ".GET_PERMISSION(?);");
            preparedStatement.setInt(1, permitId);
            list = GetPermissionDTO.getList(preparedStatement.executeQuery());
        } catch (SQLException | ConnectionPoolException e) {
            printError(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                printError(e);
            }
            connectionPool.releaseConnection(conn);
        }
        return list;
    }

    /**
     * ===========================================================
     * TABLE crl_token_blacklist
     * ===========================================================
     */
    public List<GetTokenBlacklistDTO> getTokenBlacklist(String token) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        List<GetTokenBlacklistDTO> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return list;
            preparedStatement = conn.prepareStatement("CALL " + DatabaseStrings.GET_DATABASE_NAME + ".GET_TOKEN_BLACKLIST(?);");
            preparedStatement.setString(1, token);
            list = GetTokenBlacklistDTO.getList(preparedStatement.executeQuery());
        } catch (SQLException | ConnectionPoolException e) {
            printError(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                printError(e);
            }
            connectionPool.releaseConnection(conn);
        }
        return list;
    }

    /**
     * ===========================================================
     * TABLE grl_products
     * ===========================================================
     */
    public boolean isProductExists(String code) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return false;
            preparedStatement = conn.prepareStatement("CALL " + DatabaseStrings.GET_DATABASE_NAME + ".IS_PRODUCT_EXISTS(?);");
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) if (resultSet.getInt(DatabaseStrings.GET_RESULT) > 0) return true;
        } catch (SQLException | ConnectionPoolException e) {
            printError(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                printError(e);
            }
            connectionPool.releaseConnection(conn);
        }
        return false;
    }

    /**
     * ===========================================================
     * Print errors
     * ===========================================================
     */
    private void printError(Exception ex) {
        String errMsg = ">> QueriesServices:\n" + ex;
        logger.error(errMsg);
    }
}
