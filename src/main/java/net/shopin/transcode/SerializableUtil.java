package net.shopin.transcode;

import java.io.*;

/**
 * 说明: java 序列化 相关原理
 * User: kongming
 * Date: 14-5-26
 * Time: 下午4:00
 */
public class SerializableUtil {



    public static class User implements Serializable {


        private static final ObjectStreamField[] serialPersistentFields = {
               new ObjectStreamField("firstname",String.class)
        };

        String firstname;

        String lastname;

        public User(String firstname, String lastname) {
            this.firstname = firstname;
            this.lastname = lastname;
        }

        private void writeObject(ObjectOutputStream output) throws IOException {
            output.defaultWriteObject();
            output.writeUTF("Hello World");
        }
        private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
            input.defaultReadObject();
            String value = input.readUTF();
            System.out.println(value);
        }

        @Override
        public String toString() {
            return "User{" +
                    "firstname='" + firstname + '\'' +
                    ", lastname='" + lastname + '\'' +
                    '}';
        }
    }


    public static void main(String[] args) {

        User user = new User("kong","ming");

        //序列化
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("user.bin"));
            output.writeObject(user);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //反序列化

        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("user.bin"));
            User  readUser = (User) input.readObject();
            System.out.println(readUser);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }



}
