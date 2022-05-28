package com.vectorx.springdata.util;

import com.vectorx.springdata.entities.Person;
import de.vandermeer.asciitable.AsciiTable;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 格式化输出Person信息类
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-27 21:31:36
 */
public class PrintPersonUtil
{
    public static void print(Page<Person> page) {
        printPageInfo(page);
        printPersonInfo(page.getContent());
    }

    private static void printPageInfo(Page<Person> page) {
        System.out.println("总计：" + page.getTotalElements() + "条");
        System.out.println("总页数：" + page.getTotalPages() + "页");
        System.out.println("当前页：第" + (page.getNumber() + 1) + "页");
        System.out.println("当前页容量：" + page.getSize() + "条");
        System.out.println("当前页实际数：" + page.getNumberOfElements() + "条");
        System.out.println("当前页内容：");
    }

    private static void printPersonInfo(List<Person> personList) {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow(Person.ID, Person.ADDRESSID, Person.LASTNAME, Person.EMAIL);
        at.addRule();
        for (Person person : personList) {
            at.addRow(person.getId(), person.getAddressId(), person.getLastName(), person.getEmail());
            at.addRule();
        }
        System.out.println(at.render());
    }
}
