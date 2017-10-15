/*
 * Test.java
 * Copyright (C) 2017 huang <huang@huang-desktop>
 *
 * Distributed under terms of the MIT license.
 */

public class Test
{
    public int x;
    public int y;

    public class TT {
        int t;
        int tt;
        public TT(int x, int y) {
            t = x;
            tt = y;
        }
    }

	public Test() {
		
	}

	public Test(int x, int y) {
        this.x = x;
        this.y = y;
	}

    public static void test_tt() {
    
        TT t1 = new TT(1,2);

    }
    
    public int add() {
        if (x > 0) {
            int z = 10;
            y = z + x;
            if (y >0) {
                int zz = 20;
                y = zz + x;
            }
        
        }
        return y;
    }

    public static void main(String[] args) {
    
        Test test = new Test(1,2);
        int y = test.add();
        System.out.format("The result is %d\n", y);
    }
}

