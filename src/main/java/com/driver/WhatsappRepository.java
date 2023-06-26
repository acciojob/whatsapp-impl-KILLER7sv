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
        if(users.size() == 2){
            Group group = new Group(users.get(1).getName() , users.size());
            groupUserMap.put(group , users);
            adminMap.put(group, users.get(0));
            return group;
        }

        Group group = new Group("Group" + groupUserMap.size(), users.size());
        groupUserMap.put(group , users);
        adminMap.put(group, users.get(0));
        customGroupCount++;
        return group;
    }


    public int createMessage(String content) {
        messageId++;
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        List<User> a = groupUserMap.get(group);
        boolean flag = false;
        for(User user : a){
            if(user == sender){
                flag = true;
            }
        }
        if(flag){
            return messageId;
        }else{
            throw new Exception("You are not allowed to send message");
        }
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)){
            return "Group does not exist";
        }
        if(adminMap.containsKey(group)){
            if(adminMap.get(group) != approver){
                return "Approver does not have rights";
            }
        }else{
            return "User doest Not Exist";
        }


        List<User> a = groupUserMap.get(group);
        boolean flag = false;
        for(User x : a){
            if(user == x){
                flag = true;
            }
        }
        if(flag){
            adminMap.put(group , user);
            return "SUCCESS";
        }else{
            return "User is not a participant";
        }
    }

}
