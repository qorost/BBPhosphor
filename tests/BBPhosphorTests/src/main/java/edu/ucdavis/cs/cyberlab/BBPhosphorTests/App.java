package edu.ucdavis.cs.cyberlab.BBPhosphorTests;

/**
 * Hello world!
 *
 */
public class App
{
  public static class Test
  {
      public int x;
      public int y;


  	public Test() {

  	}

  	public Test(int x, int y) {
          this.x = x;
          this.y = y;
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
    }
    public static void main( String[] args )
    {
      Test test = new Test(1,2);
      int y = test.add();
      System.out.format("The result is %d\n", y);
        System.out.println( "Hello World!" );
    }
}
