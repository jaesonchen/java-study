package com.asiainfo.designpattern.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Composite组合模式，将对象以树形结构组织起来,以达成“部分－整体” 的层次结构，使得客户端对单个对象和组合对象的使用具有一致性。
 * 典型应用：部门树形结构。
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:31:11
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Composite {

    public static void main(String[] args) {

        IComponent root = new Folder("root");
        
        IComponent folder1 = new Folder("folder1");
        IComponent folder2 = new Folder("folder2");
        IComponent leaf1 = new Leaf("leaf1");
        root.add(folder1);
        root.add(folder2);
        root.add(leaf1);
        
        IComponent folder11 = new Folder("folder11");
        IComponent folder12 = new Folder("folder12");
        IComponent leaf11 = new Leaf("leaf11");
        folder1.add(folder11);
        folder1.add(folder12);
        folder1.add(leaf11);
        
        IComponent folder21 = new Folder("folder21");
        IComponent folder22 = new Folder("folder22");
        IComponent leaf21 = new Leaf("leaf21");
        folder2.add(folder21);
        folder2.add(folder22);
        folder2.add(leaf21);
        
        System.out.print(root);
    }
    
    // 组件接口
    interface IComponent {
        public void operate();
        public void add(IComponent component);
        public void remove(IComponent component);
        public Iterator<IComponent> iterator();
        public void setDepth(int depth);
        public int getDepth();
    }
    
    // 整体组件实现
    static class Folder implements IComponent {
        
        private String name;
        private int depth = 0;
        private List<IComponent> childs = new ArrayList<IComponent>();
        public Folder(String name) {
            this.name = name;
        }
        
        @Override public int getDepth() {
            return depth;
        }
        @Override public void setDepth(int depth) {
            this.depth = depth;
        }
        @Override public void operate() {
            System.out.println("operate ...");
        }
        @Override public void add(IComponent component) {
            component.setDepth(this.depth + 1);
            this.childs.add(component);
        }
        @Override public void remove(IComponent component) {
            this.childs.remove(component);
        }
        @Override public Iterator<IComponent> iterator() {
            return childs.iterator();
        }
        @Override public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("|-").append(this.name);
            for (IComponent cp : childs) {
                sb.append("\n");
                for(int i = 0; i < cp.getDepth(); i++) {
                    sb.append("  ");
                }
                sb.append(cp);
            }       
            return sb.toString();
        }
    }
    
    // 部分组件实现
    static class Leaf implements IComponent {

        private String name;
        private int depth = 0;
        public Leaf(String name) {
            this.name = name;
        }
        
        @Override public int getDepth() {
            return depth;
        }
        @Override public void setDepth(int depth) {
            this.depth = depth;
        }
        @Override public void operate() {
            System.out.println("operate ...");
        }
        @Override public void add(IComponent component) {
            throw new UnsupportedOperationException("error in add.Leaf can not has child");
        }
        @Override public void remove(IComponent component) {
            throw new UnsupportedOperationException("error in remove.Leaf can not has child");
        }
        @Override public Iterator<IComponent> iterator() {
            return null;
        }
        @Override public String toString() {
            return "|-" + this.name;
        }
    }
}
