package lab6.var4;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.util.Random;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    // Константы, задающие размер окна приложения, если оно
    // не распахнуто на весь экран
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private JMenuItem pauseMenuItem;
    private JMenuItem resumeMenuItem;
    private JMenuItem speedMenuItem;

    Random r=new Random();

    // Поле, по которому прыгают мячи
    private Field field = new Field();

    // Конструктор главного окна приложения
    public MainFrame() {
        super("Программирование и синхронизация потоков");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        // Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);
        // Установить начальное состояние окна развернутым на весь экран
        // setExtendedState(MAXIMIZED_BOTH);

        // Создать меню
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu ballMenu = new JMenu("Мячи");
        Action addBallAction = new AbstractAction("Добавить мяч") {
            public void actionPerformed(ActionEvent event) {
                field.addBall();
            }
        };
        menuBar.add(ballMenu);
        ballMenu.add(addBallAction);

        Action superBall = new AbstractAction("Супермячик") {         // уничтожает кирпичик за один удар
            public void actionPerformed(ActionEvent event) {
                ArrayList<BouncingBall> balls=field.getBalls();
                int i=r.nextInt(balls.size());
                while(balls.get(i)==null && balls.get(i).isSuperflag())
                    i=r.nextInt(balls.size());
                int q=0;
                for(BouncingBall ball: balls)
                {
                    if(q==i) {
                        ball.superball();
                        break;
                    }
                    else
                        q++;
                }
                field.setBalls(balls);

            }
        };

        menuBar.add(ballMenu);
        ballMenu.add(superBall);

        Action add10BallAction = new AbstractAction("Добавить 10 мячей") {
            public void actionPerformed(ActionEvent event) {
                for(int i=1;i<=10;i++)
                    field.addBall();
            }
        };

        ballMenu.add(add10BallAction);

        JMenu KirpMenu = new JMenu("Кирпичики");
        Action addKirpAction = new AbstractAction("Добавить кирпичик") {
            public void actionPerformed(ActionEvent event) {
                field.addKirp();
                pauseMenuItem.setEnabled(true);
                resumeMenuItem.setEnabled(false);
            }
        };
        menuBar.add(KirpMenu);
        KirpMenu.add(addKirpAction);

        Action add10KirpAction = new AbstractAction("Добавить 10 кирпичиков") {
            public void actionPerformed(ActionEvent event) {
               for(int i=1;i<=10;i++)
                    field.addKirp();
                pauseMenuItem.setEnabled(true);
                resumeMenuItem.setEnabled(false);
            }
        };
        menuBar.add(KirpMenu);
        KirpMenu.add(add10KirpAction);

        Action delKirpAction = new AbstractAction("Удалить кирпичики") {
            public void actionPerformed(ActionEvent event) {
                field.delKirpichiky();
            }
        };

        menuBar.add(KirpMenu);
        KirpMenu.add(delKirpAction);

        JMenu controlMenu = new JMenu("Управление");
        menuBar.add(controlMenu);

        Action pauseAction = new AbstractAction("Приостановить движение мячей"){
            public void actionPerformed(ActionEvent event) {
                field.pause();
                pauseMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
                speedMenuItem.setEnabled(false);
            }
        };

        pauseMenuItem = controlMenu.add(pauseAction);
        pauseMenuItem.setEnabled(false);

        Action resumeAction = new AbstractAction("Возобновить движение") {
            public void actionPerformed(ActionEvent event) {
                field.resume();
                pauseMenuItem.setEnabled(true);
                resumeMenuItem.setEnabled(false);
                speedMenuItem.setEnabled(true);
            }
        };

        resumeMenuItem = controlMenu.add(resumeAction);
        resumeMenuItem.setEnabled(false);

        Action speedAction = new AbstractAction("Ускорить движение") {
            public void actionPerformed(ActionEvent event) {
                field.speed();
                pauseMenuItem.setEnabled(true);
                resumeMenuItem.setEnabled(false);
            }
        };

        speedMenuItem = controlMenu.add(speedAction);
        speedMenuItem.setEnabled(true);


        // Добавить в центр граничной компоновки поле Field
        getContentPane().add(field, BorderLayout.CENTER);
    }

    // Главный метод приложения
    public static void main(String[] args) {
        // Создать и сделать видимым главное окно приложения
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
