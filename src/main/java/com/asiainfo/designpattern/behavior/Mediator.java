package com.asiainfo.designpattern.behavior;

import java.util.HashMap;
import java.util.Map;

/**
 * 用一个中介对象来封装一系列的对象交互，中介者使各对象不需要显式地相互引用，从而使其耦合松散，而且可以独立地改变它们之间的交互。
 * 聊天室应用使用了中介模式
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:47:09
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Mediator {

    public static void main(String[] args) {

        AbstractChatroom happyChat = new ChatGroup();
        Member member1, member2, member3, member4, member5;
        member1 = new DiamondMember("张三");
        member2 = new DiamondMember("李四");
        member3 = new CommonMember("王五");
        member4 = new CommonMember("小芳");
        member5 = new CommonMember("小红");
        
        happyChat.register(member1);
        happyChat.register(member2);
        happyChat.register(member3);
        happyChat.register(member4);
        happyChat.register(member5);
        
        member1.sendText("李四", "李四，你好！");
        member2.sendText("张三", "张三，你好！");
        member1.sendText("李四", "今天天气不错，有日！");
        member2.sendImage("张三", "一个很大很大的太阳");
        member2.sendImage("张三", "太阳");
        member3.sendText("小芳", "还有问题吗？");
        member3.sendText("小红", "还有问题吗？");
        member4.sendText("王五", "没有了，谢谢！");
        member5.sendText("王五", "我也没有了！");
        member5.sendImage("王五", "谢谢");
    }

    // mediator
    abstract static class AbstractChatroom {
        public abstract void register(Member member);
        public abstract void sendText(String from, String to, String message);
        public abstract void sendImage(String from, String to, String image);
    }
    // mediator implement
    static class ChatGroup extends AbstractChatroom {
        
        private Map<String, Member> members = new HashMap<>();
        
        // register member
        public void register(Member member) {
            if (!members.containsKey(member.getName())) {
                members.put(member.getName(), member);
                member.setChatroom(this);
            }
        }
        @Override
        public void sendText(String from, String to, String message) {
            
            Member member = members.get(to);
            member.receiveText(from, message.replace("日", "*"));
        }
        @Override
        public void sendImage(String from, String to, String image) {

            Member member;
            if (image.length() > 5) {
                member = members.get(from);
                member.receiveText("系统", "图片太大，发送失败！");
            } else {
                member = members.get(to);
                member.receiveImage(from, image);
            }
        }
    }
    // member
    abstract static class Member {
        
        protected AbstractChatroom chatroom;
        protected String name;

        public Member(String name) {
            this.name = name;
        }
        public AbstractChatroom getChatroom() {
            return chatroom;
        }
        public void setChatroom(AbstractChatroom chatroom) {
            this.chatroom = chatroom;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public abstract void sendText(String to, String message);
        public abstract void sendImage(String to, String image);

        public void receiveText(String from, String message) {
            System.out.println(from + "发送文本给" + this.name + "，内容为：" + message);
        }
        public void receiveImage(String from, String image) {
            System.out.println(from + "发送图片给" + this.name + "，图片为：" + image);
        }   
    }
    // member implement
    static class CommonMember extends Member {
        
        public CommonMember(String name) {
            super(name);
        }
        @Override
        public void sendText(String to, String message) {
            System.out.println("普通会员发送信息：" + message);
            chatroom.sendText(name, to, message);
        }
        @Override
        public void sendImage(String to, String image) {
            System.out.println("普通会员不能发送图片！");
        }
    }
    // member implement
    static class DiamondMember extends Member {
        
        public DiamondMember(String name) {
            super(name);
        }
        @Override
        public void sendText(String to, String message) {
            System.out.println("钻石会员发送信息：" + message);
            chatroom.sendText(name, to, message);
        }
        @Override
        public void sendImage(String to, String image) {
            System.out.println("钻石会员发送图片：" + image);
            chatroom.sendImage(name, to, image);
        }
    }
}
