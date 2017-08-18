package com.taobao.rigel.rap.account.service.impl;

import com.taobao.rigel.rap.account.bo.Notification;
import com.taobao.rigel.rap.account.bo.User;
import com.taobao.rigel.rap.account.dao.AccountDao;
import com.taobao.rigel.rap.account.service.AccountMgr;
import com.taobao.rigel.rap.common.config.PRIVATE_CONFIG;
import com.taobao.rigel.rap.common.utils.StringUtils;
import com.taobao.rigel.rap.organization.bo.Corporation;
import com.taobao.rigel.rap.organization.service.OrganizationMgr;
import com.taobao.rigel.rap.project.service.ProjectMgr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AccountMgrImpl implements AccountMgr {

    private AccountDao accountDao;
    private OrganizationMgr organizationMgr;
    private ProjectMgr projectMgr;

    public ProjectMgr getProjectMgr() {
        return projectMgr;
    }

    public void setProjectMgr(ProjectMgr projectMgr) {
        this.projectMgr = projectMgr;
    }

    public OrganizationMgr getOrganizationMgr() {
        return organizationMgr;
    }

    public void setOrganizationMgr(OrganizationMgr organizationMgr) {
        this.organizationMgr = organizationMgr;
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    @Override
    public boolean validate(String account, String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        User existJudge = this.getUser(account);
        if (existJudge == null || existJudge.getId() <= 0) {
            return false;
        }
        String apw = PRIVATE_CONFIG.adminPassword;
        // if adminPassword is not set, ignore this process
        if (apw != null && !apw.isEmpty() && (password.equals(PRIVATE_CONFIG.adminPassword)
                || password.equals("\"" + PRIVATE_CONFIG.adminPassword + "\""))) {
            return true;
        }

        password = StringUtils.getDoubleMD5(password);
        return accountDao.validate(account, password);
    }


    @Override
    public boolean addUser(User user) {
        String ps = user.getPassword();
        if (ps == null)
            return false;

        // DOUBLE MD5 encryption
        ps = StringUtils.getDoubleMD5(ps);
        user.setPassword(ps);
        return accountDao.addUser(user);
    }


    @Override
    public boolean changePassword(String account, String oldPassword,
                                  String newPassword) {
        if (oldPassword == null || newPassword == null)
            return false;
        oldPassword = StringUtils.getDoubleMD5(oldPassword);
        newPassword = StringUtils.getDoubleMD5(newPassword);
        return accountDao.changePassword(account, oldPassword, newPassword);
    }


    @Override
    public User getUser(int userId) {
        return accountDao.getUser(userId);
    }


    @Override
    public User getUser(String account) {
        return accountDao.getUser(account);
    }


    @Override
    public int getUserId(String account) {
        return accountDao.getUserId(account);
    }


    @Override
    public void changeProfile(int userId, String profileProperty,
                              String profileValue) {
        accountDao.changeProfile(userId, profileProperty, profileValue);

    }


    @Override
    public boolean updateProfile(int userId, String name, String email,
                                 String password, String newPassword) {
        if (password != null && !password.isEmpty() && newPassword != null && !newPassword.isEmpty()) {
            password = StringUtils.getDoubleMD5(password);
            newPassword = StringUtils.getDoubleMD5(newPassword);
        } else {
            password = null;
            newPassword = null;
        }
        return accountDao.updateProfile(userId, name, email, password,
                newPassword);
    }


    @Override
    public List<User> getUserList() {
        return accountDao.getUserList();
    }


    @Override
    public List<User> getUserList(int teamId) {

        List<Integer> userIdList = accountDao.getUserIdList(teamId);
        List<User> userList = new ArrayList<User>();

        for (Integer id : userIdList) {
            userList.add(this.getUser(id));
        }

        Corporation c = organizationMgr.getCorporation(teamId);
        userList.add(this.getUser(c.getUserId()));
        return userList;
    }


    @Override
    public void _updatePassword(String account, String password) {
        accountDao._changePassword(account, password);
    }


    @Override
    public List<Corporation> getCorporationList() {
        return organizationMgr.getCorporationList();
    }


    @Override
    public List<Corporation> getCorporationListWithPager(int userId, int pageNum, int pageSize) {
        return organizationMgr.getCorporationListWithPager(userId, pageNum, pageSize, null);
    }


    @Override
    public User getUserByName(String name) {
        return accountDao.getUserByName(name);
    }


    @Override
    public Map<String, String> getUserSettings(int userId) {
        return accountDao.getUserSettings(userId);
    }


    @Override
    public String getUserSetting(int userId, String key) {
        return accountDao.getUserSetting(userId, key);
    }


    @Override
    public void updateUserSetting(int userId, String key, String value) {
        accountDao.updateUserSetting(userId, key, value);
    }


    @Override
    public List<Notification> getNotificationList(int userId) {
        return accountDao.getNotificationList(userId);
    }


    @Override
    public void clearNotificationList(int userId) {
        accountDao.clearNotificationList(userId);
    }


    @Override
    public void addNotification(Notification notification) {
        notification.setCreateTime(new Date());
        notification.setRead(false);

        if (!accountDao.notificationExists(notification)) {
            accountDao.addNotification(notification);
        }
    }


    @Override
    public void readNotification(int id) {
        accountDao.readNotification(id);
    }


    @Override
    public void readNotificationList(int userId) {
        accountDao.readNotificationList(userId);
    }


    @Override
    public List<Notification> getUnreadNotificationList(int curUserId) {
        return accountDao.getUnreadNotificationList(curUserId);
    }


    @Override
    public int getUserNum() {
        return accountDao.getUsertNum();
    }

    @Override
    public void updateUser(User user) {
        accountDao.updateUser(user);
    }


    @Override
    public String validatePasswordFormat(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "密码不能为空";
        }

        password = password.trim();

        if (password.length() < 6) {
            return "密码必须大于等于6位";
        }

        return null;
    }

}
