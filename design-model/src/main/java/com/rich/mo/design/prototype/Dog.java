package com.rich.mo.design.prototype;

import lombok.Data;

import java.io.*;

@Data
public class Dog  implements Serializable {

    private GuiBin guiBin;
    private ZhongHuaTianYuan zhongHuaTianYuan;


    public Dog deepClone(){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);


            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            Dog dog = (Dog)ois.readObject();

            return dog;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;

    }

    public static void main(String[] args) {
        GuiBin guiBin = new GuiBin();
        guiBin.setWeight(14);

        ZhongHuaTianYuan zhongHuaTianYuan = new ZhongHuaTianYuan();
        zhongHuaTianYuan.setWeight(30);

        Dog dog = new Dog();
        dog.setGuiBin(guiBin);
        dog.setZhongHuaTianYuan(zhongHuaTianYuan);


        Dog dog1 = dog.deepClone();

        System.out.println(dog==dog1);
        System.out.println(dog.guiBin ==dog1.guiBin);
        System.out.println(dog.zhongHuaTianYuan ==dog1.zhongHuaTianYuan);


    }

}
