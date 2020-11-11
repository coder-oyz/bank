package com.oyz.test5;
import java.util.Arrays;
import java.util.Scanner;
public class BankClass {
	int Rnum;//资源的种类数
	int Cnt;//进程数 
	
	//boolean[] Finish;//完成序列
	int[] Available;
    int[][] Max;
    int[][] Alloction;
    int[][] Need;
    int[][] Request;
    int[] Work;

    int pid = 0;//进程编号
    Scanner in = new Scanner(System.in);

    public BankClass() {
    	System.out.println("请输入进程数：");
    	Cnt=in.nextInt();//进程数
    }
    
    public void setAvailable() {
    	System.out.println("请输入资源的种类数：");
    	Rnum=in.nextInt();//资源的种类数
    	System.out.println("请输入初始各资源个数：");
    	Available=new int [Rnum];
    	for(int i=0;i<Rnum;i++) {
    		Available[i]=in.nextInt();
    	}
    }
    
    public void setSystemVariable(){//设置各初始系统变量，并判断是否处于安全状态。
    	setAvailable();
        setMax();
        setAlloction();
        printSystemVariable();
        SecurityAlgorithm();
    }

    public void setMax() {//设置Max矩阵
    	Max=new int [Cnt][Rnum];
        System.out.println("请设置各进程的最大需求矩阵Max：");
        for (int i = 0; i < Cnt; i++) {
            System.out.println("请输入进程P" + i + "的最大资源需求量：");
            for (int j = 0; j < Rnum; j++) {
                Max[i][j] = in.nextInt();
            }
        }
    }

    public void setAlloction() {//设置已分配矩阵Alloction
    	Alloction =new int[Cnt][Rnum];
    	Need =new int[Cnt][Rnum];
        System.out.println("请设置请各进程分配矩阵Alloction：");
        for (int i = 0; i < Cnt; i++) {
            System.out.println("请输入进程P" + i + "的分配资源量：");
            for (int j = 0; j < Rnum; j++) {
                Alloction[i][j] = in.nextInt();
            }
        }
        System.out.println("Available=Available-Alloction.");
        System.out.println("Need=Max-Alloction.");
        for (int i = 0; i < Rnum; i++) {//设置Alloction矩阵
            for (int j = 0; j < Cnt; j++) {
                Available[i] = Available[i] - Alloction[j][i];
            }
        }
        for (int i = 0; i < Cnt; i++) {//设置Need矩阵
            for (int j = 0; j < Rnum; j++) {
                Need[i][j] = Max[i][j] - Alloction[i][j];
            }
        }
    }

    public void printSystemVariable(){
        System.out.println("此时资源分配量如下：");
        System.out.println("进程  "+"   Max   "+"   Alloction "+"    Need  "+"     Available ");
        for(int i=0;i<Cnt;i++){
            System.out.print("P"+i+"  ");
            for(int j=0;j<Rnum;j++){
               System.out.print(Max[i][j]+"  "); 
            }
            System.out.print("|  ");
            for(int j=0;j<Rnum;j++){
               System.out.print(Alloction[i][j]+"  "); 
            }
            System.out.print("|  ");
            for(int j=0;j<Rnum;j++){
               System.out.print(Need[i][j]+"  "); 
            }
            System.out.print("|  ");
            if(i==0){
                for(int j=0;j<Rnum;j++){
                    System.out.print(Available[j]+"  ");
                }
            }
            System.out.println();
        }
    }

    public void setRequest() {//设置请求资源量Request
    	Request=new int[Cnt][Rnum];
        System.out.println("请输入请求资源的进程编号：");
        pid= in.nextInt();//设置全局变量进程编号pid
        System.out.println("请输入请求各资源的数量：");
        for (int j = 0; j < Rnum; j++) {
            Request[pid][j] = in.nextInt();
        }
        System.out.print("即进程P" + pid + "对各资源请求Request：(");
        for(int i=0;i<Rnum;i++) {
        	if(i!=Rnum-1) {
        		System.out.print(Request[pid][i] + ",");
        	}else {
        		System.out.println(Request[pid][i] + ")");
        	}
        } 
        BankerAlgorithm();
    }

    public void BankerAlgorithm() {//银行家算法
        boolean T=true;
        
        if (isBigNeed(pid)) {//判断Request是否小于Need
            if (isBigAvailable(pid)) {//判断Request是否小于Alloction
                for (int i = 0; i < Rnum; i++) {
                    Available[i] -= Request[pid][i];
                    Alloction[pid][i] += Request[pid][i];
                    Need[pid][i] -= Request[pid][i];
                }

            } else {
                System.out.println("当前没有足够的资源可分配，进程P" + pid + "需等待。");
               T=false;
            }
        } else {
            System.out.println("进程P" + pid + "请求已经超出最大需求量Need.");
            T=false;
        }

       if(T==true){
        printSystemVariable(); 
        System.out.println("现在进入安全算法：");
        SecurityAlgorithm();
       }
    }


    public void SecurityAlgorithm() {//安全算法
        boolean []Finish = new boolean[Cnt];//初始化Finish
        Work=new int[Rnum];
        int count = 0;//完成进程数
        int circle=0;//循环圈数
        int[] S=new int[Cnt];//安全序列
        for (int i = 0; i < Rnum; i++) {//设置工作向量
            Work[i] = Available[i];
        }
        boolean flag = true;//用来判断第一次进入的
        while (count < Cnt) {
            if(flag){
                System.out.println("进程  "+"   Work  "+"   Need "+"    Alloction  "+"     Work+Alloction "+"  Finish  ");
                flag = false;
            }
            for (int i = 0; i < Cnt; i++) {//判断是否有可以分派的，没有就是不安全
            	boolean temp=isEnough(Finish,i);
            		if (temp) {//判断条件
                        System.out.print("P"+i+"  ");
                        for (int k = 0; k < Rnum; k++){
                            System.out.print(Work[k]+"  ");
                        }
                        System.out.print("|  ");
                        for (int j = 0; j<Rnum;j++){
                        Work[j]+=Alloction[i][j];
                        }
                        Finish[i]=true;//当当前进程能满足时
                        S[count]=i;//设置当前序列排号     安全序列

                        count++;//满足进程数加1
                        for(int j=0;j<Rnum;j++){
                            System.out.print(Need[i][j]+"  "); 
                         }
                       
                        System.out.print("|  ");
                        for(int j=0;j<Rnum;j++){
                            System.out.print(Alloction[i][j]+"  "); 
                        }
                        System.out.print("|  ");
                        for(int j=0;j<Rnum;j++){
                           System.out.print(Work[j]+"  "); 
                        }
                        System.out.println("\tTure");
                   
            	}	
                

            }
            circle++;//循环圈数加1

            if(count==Cnt){//判断是否满足所有进程需要
                System.out.print("此时存在一个安全序列：");
                for (int i = 0; i<Cnt;i++){//输出安全序列
                    System.out.print("P"+S[i]+" ");
                }
                System.out.println("此刻是安全的，故当前可分配！"); 
                System.out.println("Available:"+Arrays.toString(Available));
                break;//跳出循环    此时没直接退出循环，不返还资源，即已分配完成
            }
            if(count<circle){//判断完成进程数是否小于循环圈数
                //count=0;
                System.out.println("当前系统处于不安全状态，故不存在安全序列。");
                for (int i = 0; i < Rnum; i++) {//不将进程i申请的资源分配给进程i，恢复原来的资源分配状态
	                Available[i] += Request[pid][i];
	                Alloction[pid][i] -= Request[pid][i];
	                Need[pid][i] += Request[pid][i];
                }
                
                break;//跳出循环
            }
        }
    }
    
    private boolean isBigAvailable(int pid) {
    	if(Request[pid][0] > Available[0]) {
    		return false;
    	}
    	return true;
    }
    
    private boolean isBigNeed(int pid) {//判断Request是否小于Need
    	for(int i=0;i<Rnum;i++) {
    		if(Request[pid][i]>Need[pid][i]) {
    			return false;
    		}
    	}
    	return true;
    }

	private boolean isEnough(boolean []Finish,int i) {//判断是否资源足够，能够分配
		for(int g = 0; g<Rnum; g++) {
        	if (Finish[i]!=false || Need[i][g]>Work[g]) {
        		return false;
        	}
		}
		return true;
	}
}
