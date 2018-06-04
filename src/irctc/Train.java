/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irctc;

/**
 *
 * @author Administrator
 */
public class Train {
    int no;
    String name,f,t;
    int ac1,ac2,ac3,ac4,ac5,sl1,sl2,sl3,sl4,sl5,avlac,avlsl;
    public Train() {
    }

    public Train(int no, String name, String f, String t) {
        this.no = no;
        this.name = name;
        this.f = f;
        this.t = t;
    }
    public String toString(){
        return (no+"\t"+name+"\t"+f+"\t"+t+"\t"+ac1+"\t"+ac2+"\t"+ac3+"\t"+ac4+"\t"+ac5
                +"\t"+sl1+"\t"+sl2+"\t"+sl3+"\t"+sl4+"\t"+sl5+"\t"+avlac+"\t"+avlsl);
    }
}
