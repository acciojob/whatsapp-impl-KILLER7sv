package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception {
        if (userMobile.contains(mobile)){
            throw new Exception("User Already Exists !!");
        }

        userMobile.add(mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        if(users.size() > 2){
            Group group = new Group("Group" + groupUserMap.size(), users.size());
            groupUserMap.put(group , users);
            return group;
        }
        Group group = new Group(users.get(1).getName() , users.size());
        groupUserMap.put(group , users);
        return group;
    }
}
