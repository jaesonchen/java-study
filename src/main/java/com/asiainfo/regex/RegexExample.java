/**
 * @Title:  RegexExample.java
 * @Package: com.asiainfo.regex
 * @Description: TODO
 * @author: chenzq
 * @date:   2019年2月5日 下午2:00:56
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
package com.asiainfo.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**   
 * @Description: java regex包使用
 * 
 *  Pattern 对象是一个正则表达式的编译表示。Pattern 类没有公共构造方法。要创建一个 Pattern 对象，
 *  你必须首先调用其公共静态编译方法，它返回一个 Pattern 对象。该方法接受一个正则表达式作为它的第一个参数。
 *  
 *  Matcher 对象是对输入字符串进行解释和匹配操作的引擎。与Pattern 类一样，Matcher 也没有公共构造方法。
 *  你需要调用 Pattern 对象的 matcher 方法来获得一个 Matcher 对象。
 * 
 * 常用正则表达式：
 * 一个或多个汉字          ^[\u0391-\uFFE5]+$
 * 邮政编码                    ^[0-9]{6}$或\d{6}
 * qq号码                      ^[1-9]\d{4,10}$
 * 邮箱                           [a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}
 * 手机号码                     ^1[3458]\d{9}
 * 身份证号码                ^(\d{6})(18|19|20)?(\d{2})([01]\d)([0123]\d)(\d{3})(\d|X|x)?$
 * 
 * @author chenzq  
 * @date 2019年2月5日 下午2:00:56
 * @version V1.0  
 */
public class RegexExample {

    public static void main(String[] args) {

        //Pattern.matches匹配子串
        String line = "I am noob from runoob.com.";
        String pattern = ".*runoob.*";
        System.out.println("字符串中是否包含了 'runoob' 子字符串? " + Pattern.matches(pattern, line));
        
        //Matcher.group
        //捕获组是把多个字符当一个单独单元进行处理的方法，它通过对括号内的字符分组来创建。捕获组是通过从左至右计算其开括号来编号。
        //可以通过调用 matcher 对象的 groupCount 方法来查看表达式有多少个分组。groupCount 方法返回一个 int 值，表示matcher对象当前有多个捕获组。
        //还有一个特殊的组（group(0)），它总是代表整个表达式。该组不包括在 groupCount 的返回值中。
        line = "This order was placed for QT3000! OK?";
        pattern = "(\\D*)(\\d+)(.*)";
        // 创建 Pattern 对象
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(line);
        //尝试查找与该模式匹配的输入序列的下一个子序列
        if (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                System.out.println("group(" + i + "): " + m.group(i) );
            }
        } else {
            System.out.println("NO MATCH");
        }
        System.out.println("==============================");
        
        //Matcher.start、Matcher.end 返回匹配的初始索引和最后匹配字符之后的偏移量。
        line = "cat cat cat cattie cat";
        pattern = "\\bcat\\b";
        p = Pattern.compile(pattern);
        m = p.matcher(line);
        int count = 0;
        while (m.find()) {
            count++;
            System.out.println("Match number: " + count);
            System.out.println("start(): " + m.start());
            System.out.println("end(): " + m.end());
        }
        System.out.println("==============================");
        
        
        //Matcher.group(name)
        //命名组捕获，格式  (?<year>\\d{4})
        
        
        //Matcher.matches匹配整个输入序列
        //Matcher.lookingAt从第一个字符开始匹配序列
        String regex = "foo";
        String input = "fooooooooooooooooo";
        String input2 = "ooooofoooooooooooo";
        p = Pattern.compile(regex);
        Matcher matcher = p.matcher(input);
        Matcher matcher2 = p.matcher(input2);
        System.out.println("lookingAt(): " + matcher.lookingAt());
        System.out.println("matches(): " + matcher.matches());
        System.out.println("lookingAt(): " + matcher2.lookingAt());
        
    }
}
