package com.dldorg.user;

import com.cloud.common.core.utils.BeanUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
public class UserCompare {

    public static List<User> getCompare(String orgFullName, List<Users> dld, List<Users> sg) {
        List<User> userList = new ArrayList<>();
        if (dld != null && dld.size() > 0) {
            for (Users users : dld) {
                User user = new User();
                user.setOrgFullName(orgFullName);
                user.setDName(users.getName());
                user.setDUserName(users.getTel());
                user.setDId(users.getId().toString());
                Users us = getUsers(sg, users.getName(), users.getTel());
                if (us != null) {
                    user.setSName(us.getName());
                    user.setSUserName(us.getUserName());
                    user.setSId(us.getId().toString());
                    user.addSize();
                } else {
                    user.setSize(0);
                }
                userList.add(user);
            }
            userList = userList.stream().sorted(Comparator.comparing(User::getSize).reversed()).collect(Collectors.toList());
            for (User user : userList) {
                if (user.getSId() == null) {
                    Users us = getUsers(sg);
                    if (us != null) {
                        user.setSName(us.getName());
                        user.setSUserName(us.getUserName());
                        user.setSId(us.getId().toString());
                        user.addSize();
                    }
                }
            }
        }
        if (sg != null && sg.size() > 0) {
            for (Users us : sg) {
                User u = new User();
                u.setOrgFullName(orgFullName);
                u.setSName(us.getName());
                u.setSUserName(us.getUserName());
                u.setSId(us.getId().toString());
                userList.add(u);
            }
        }
        return userList;
    }

    private static Users getUsers(List<Users> sg) {
        Users users = null;
        if (sg != null && sg.size() > 0) {
            Users u = sg.get(0);
            users = BeanUtil.copyProperties(u, Users::new);
            sg.remove(u);
        }
        return users;
    }

    private static Users getUsers(List<Users> sg, String name, String tel) {
        Users users = null;
        if(tel == null){
            tel = "";
        }
        if (sg != null && sg.size() > 0) {
            for (Users u : sg) {
                try {
                    if (u.getName().equals(name) || u.getTel().equals(tel) || (u.getUserName() != null && u.getUserName().indexOf(tel) >= 0)) {
                        users = BeanUtil.copyProperties(u, Users::new);
                        sg.remove(u);
                        break;
                    }
                }catch (Exception e){
                    System.out.println(u);
                    System.out.println(tel);
                    e.printStackTrace();
                }
            }
        }
        return users;
    }

}
