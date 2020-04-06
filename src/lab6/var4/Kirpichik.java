package lab6.var4;
        import java.awt.*;
        import java.awt.geom.GeneralPath;
        import java.util.Random;


public class Kirpichik {


    private static final int minlength = 50;
    private static final int maxlength = 100;
    private static final int minwidth = 50;
    private static final int maxwidth = 100;


    private Field field;
    private  int length;
    private  int width;
    private Color color;


    private int prochnost = 10;
    public   int flagNow = prochnost;
    public   int flagNext = 9;


    private int x;
    private int y;

    Random r=new Random();

    // Конструктор класса
    public Kirpichik(Field field) {
        // Необходимо иметь ссылку на поле, по которому прыгает мяч,
        // чтобы отслеживать выход за его пределы
        this.field = field;
        // Кирпичик случайного размера
        length= r.nextInt(maxlength-minlength)+minlength;
        width= r.nextInt(maxwidth-minwidth)+minwidth;
        // Цвет выбирается случайно
        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        // Начальное положение случайно
        x =r.nextInt(field.getArrayH());
        y = r.nextInt(field.getArrayW());

    }

    public int getLength() {
        return length;
    }

    public  int getWidth() {
        return width;
    }

    public int getProchnost() { return prochnost; }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Метод прорисовки самого себя
    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        GeneralPath kirp = new GeneralPath();
        kirp.moveTo(x, y);
        kirp.lineTo(x, y + width);
        kirp.lineTo(x + length, y + width);
        kirp.lineTo(x + length, y);
        kirp.closePath();
        canvas.draw(kirp);
        canvas.fill(kirp);
        canvas.setColor(Color.BLACK);
        Font myFont = new Font("Broadway", Font.BOLD, 24);
        canvas.setFont(myFont);
        if (flagNow == flagNext) {
            flagNext--;
            prochnost--;
        }
        String s = String.valueOf(prochnost);
        canvas.drawString(s, (int) x + 10, (int) y + 20);
    }
}

