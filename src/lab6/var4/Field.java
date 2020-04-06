package lab6.var4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Field extends JPanel {

    // Флаг приостановленности движения
    private boolean paused;
    private boolean paused1;
    private boolean resumeLol;


    // Динамический список скачущих мячей
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);


    // Размеры матрицы
    private static  int arrayH=450;
    private static  int arrayW=450;

    // Массив кирпичиков
    private Kirpichik[][] array_kirp= new Kirpichik[arrayH][arrayW];


    // Класс таймер отвечает за регулярную генерацию событий ActionEvent
    // При создании его экземпляра используется анонимный класс,
    // реализующий интерфейс ActionListener
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            // Задача обработчика события ActionEvent - перерисовка окна
            repaint();
        }
    });


    public static int getArrayH() {
        return arrayH;
    }

    public static int getArrayW() {
        return arrayW;
    }

    public Kirpichik[][] getArray_kirp() {
        return array_kirp;
    }

    public ArrayList<BouncingBall> getBalls() { return balls; }

    public void setBalls(ArrayList<BouncingBall> balls) { this.balls = balls; }

    // удаляет кирпичик
    public void deletekirp(int x,int y)
    {
        array_kirp[x][y]=null;

    }


    // Конструктор класса
    public Field() {
        // Установить цвет заднего фона
        setBackground(Color.BLACK);
        // Запустить таймер
        repaintTimer.start();
    }



    // Унаследованный от JPanel метод перерисовки компонента
    public void paintComponent(Graphics g) {
        // Вызвать версию метода, унаследованную от предка
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        // Последовательно запросить прорисовку от всех мячей из списка
        for (BouncingBall ball : balls) {
            ball.paint(canvas);
        }
        // Последовательно рисуем все кирпичики
        for(int i=0;i<arrayH;i++)
            for(int j=0;j<arrayW;j++)
                if (array_kirp[i][j] != null)
                    array_kirp[i][j].paint(canvas);

    }

    // Метод добавления нового мяча в список
    public void addBall() {
        //Заключается в добавлении в список нового экземпляра BouncingBall
        // Всю инициализацию положения, скорости, размера, цвета
        // BouncingBall выполняет сам в конструкторе
        balls.add(new BouncingBall(this));

    }

    // Метод добавления нового кирпичика в массив
    public void addKirp() {
        while(true) {
            Kirpichik kirp = new Kirpichik(this);

            if (array_kirp[kirp.getX()][kirp.getY()] == null) {
                array_kirp[kirp.getX()][kirp.getY()] = kirp;
                break;
            }
        }
    }
    public void delKirpichiky()
    {
        for(int i=0;i<arrayH;i++)
            for(int j=0;j<arrayW;j++)
                if(array_kirp[i][j]!=null)
                    array_kirp[i][j]=null;
    }


    public void pause() {
        // Включить режим паузы
        paused1 = true;
        paused = true;
        resumeLol = false;

    }

    // Метод синхронизированный, т.е. только один поток может
    // одновременно быть внутри
    public synchronized void resume() {
        // Выключить режим паузы
        paused = false;
        paused1 = false;
        // Будим все ожидающие продолжения потоки
        notifyAll();
    }



    // Режим ускорения
    public synchronized void speed() {
        for(BouncingBall ball: balls)
        {
            ball.setSpeedX(ball.getSpeedX()+4);
            ball.setSpeedY(ball.getSpeedY()+4);
            ball.setX(ball.getX()+ball.getSpeedX());
            ball.setY(ball.getY()+ball.getSpeedY());
        }
    }

    // Синхронизированный метод проверки, может ли мяч двигаться
    // (не включен ли режим паузы?)
    public synchronized void canMove(BouncingBall ball) throws
            InterruptedException {

        if (paused) {
            if (ball.getRadius() < 10)
                wait();
        }
        if (paused1)
            if (resumeLol) {
                if (ball.getRadius() > 10)
                    wait();
            } else
                wait();

        // Если режим паузы включен, то поток, зашедший
        // внутрь данного метода, засыпает
        //wait();
    }

}


