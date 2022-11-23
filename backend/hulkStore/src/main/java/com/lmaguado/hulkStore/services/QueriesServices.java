package com.lmaguado.hulkStore.services;

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
     * ===================================================
     * PACKAGE OF crl_params TABLE
     * ===================================================
     */

    public String createParam(String key, String value) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String rtn = null;
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return null;
            preparedStatement = conn.prepareStatement("CALL hulk_store_db.CREATE_PARAM(?, ?);");
            preparedStatement.setString(1, key);
            preparedStatement.setString(2, value);
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

    public String getGlobalParam(String key) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String rtn = null;
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return null;
            preparedStatement = conn.prepareStatement("CALL hulk_store_db.GET_PARAM(?);");
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
     * ===================================================
     * PACKAGE OF crl_token_blacklist TABLE
     * ===================================================
     */

    public List<GetTokenBlacklistDTO> getTokenBlacklist(String token) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        List<GetTokenBlacklistDTO> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return list;
            preparedStatement = conn.prepareStatement("CALL hulk_store_db.GET_TOKEN_BLACKLIST(?);");
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
     * ===================================================
     * PACKAGE OF grl_permissions TABLE
     * ===================================================
     */

    public List<GetPermissionDTO> getPermissions() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        List<GetPermissionDTO> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return list;
            preparedStatement = conn.prepareStatement("CALL hulk_store_db.GET_PERMISSIONS();");
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

    public List<GetPermissionDTO> getPermission(int permitId) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        List<GetPermissionDTO> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return list;
            preparedStatement = conn.prepareStatement("CALL hulk_store_db.GET_PERMISSION(?);");
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
     * ===================================================
     * PACKAGE OF grl_users TABLE
     * ===================================================
     */

    public long createUser(String email, String password) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        long userId = -1;
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return userId;
            preparedStatement = conn.prepareStatement("CALL hulk_store_db.CREATE_USER(?, ?);");
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) userId = resultSet.getLong("USER_ID");
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

    public List<GetUserDTO> getUserByUserId(long userId) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        List<GetUserDTO> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return list;
            preparedStatement = conn.prepareStatement("CALL hulk_store_db.GET_USER_BY_ID(?);");
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

    public List<GetUserDTO> getUserByEmail(String email) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        List<GetUserDTO> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return list;
            preparedStatement = conn.prepareStatement("CALL hulk_store_db.GET_USER_BY_EMAIL(?);");
            preparedStatement.setString(1, email);
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
            preparedStatement = conn.prepareStatement("CALL hulk_store_db.GET_USER_PASS(?);");
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

    public long isUserExists(String email) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        long userExits = -1;
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return userExits;
            preparedStatement = conn.prepareStatement("CALL hulk_store_db.IS_USER_EXISTS(?);");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) userExits = resultSet.getInt("RESULT");
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

    /**
     * ===================================================
     * GENERAL ERROR PRINT
     * ===================================================
     */

    private void printError(Exception ex) {
        String errMsg = ">> QueriesServices:\n" + ex;
        logger.error(errMsg);
    }
}
