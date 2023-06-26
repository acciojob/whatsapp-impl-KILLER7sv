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
            throw new Exception("User already exists");
        }

        userMobile.add(mobile);
        User user = new User(name , mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        if(users.size() == 2){
            Group group = new Group(users.get(1).getName() , users.size());
            groupUserMap.put(group , users);
            adminMap.put(group, users.get(0));
            groupMessageMap.put(group , new ArrayList<Message>());
            return group;
        }
        this.customGroupCount+=1;
        Group group = new Group(new String("Group" + this.customGroupCount), users.size());
        groupUserMap.put(group , users);
        adminMap.put(group, users.get(0));
        groupMessageMap.put(group , new ArrayList<Message>());
        return group;
    }


    public int createMessage(String content) {
        this.messageId += 1;
        Message message = new Message(messageId , content);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(adminMap.containsKey(group)){
            List<User> a = groupUserMap.get(group);
            boolean flag = false;
            for(User user : a){
                if(user.equals(sender)){
                    flag = true;
                    break;
                }
            }
            if(flag){
                senderMap.put(message , sender);
                List<Message> messages = groupMessageMap.get(group);
                messages.add(message);
                groupMessageMap.put(group , messages);
                return messages.size();
            }
            throw new Exception("You are not allowed to send message");
        }
        throw new Exception("Group does not exist");
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(adminMap.containsKey(group)){
            if(adminMap.get(group).equals(approver)){
                List<User> a = groupUserMap.get(group);
                boolean flag = false;
                for(User x : a){
                    if(user == x){
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    adminMap.put(group , user);
                    return "SUCCESS";
                }
                throw new Exception("User is not a participant");
            }
            throw new Exception("Approver does not have rights");
        }
        throw new Exception("Group does not exist");
    }

}
