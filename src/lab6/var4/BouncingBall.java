package lab6.var4;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BouncingBall implements Runnable {

    // Максимальный радиус, который может иметь мяч
    private static final int MAX_RADIUS = 20;
    // Минимальный радиус, который может иметь мяч
    private static final int MIN_RADIUS = 15;
    // Максимальная скорость, с которой может летать мяч
    private static final int MAX_SPEED = 30;

    private Field field;
    private double radius;
    private Color color;
    // Флаг супер или нет
    private boolean superflag=false;

    // Текущие координаты мяча
    private double x;
    private double y;

    // Вертикальная и горизонтальная компонента скорости
    private int speed;
    private double speedX;
    private double speedY;



    // Конструктор класса BouncingBall
    public BouncingBall(Field field) {
        // Необходимо иметь ссылку на поле, по которому прыгает мяч,
        // чтобы отслеживать выход за его пределы

        // через getWidth(), getHeight()
        this.field = field;
        // Радиус мяча случайного размера
        radius = new Double(Math.random()*(MAX_RADIUS - MIN_RADIUS)).intValue() + MIN_RADIUS;
        // Абсолютное значение скорости зависит от диаметра мяча,
        // чем он больше, тем медленнее

        speed = new Double(Math.round(5*MAX_SPEED / radius)).intValue();


        if (speed>MAX_SPEED) {
            speed = MAX_SPEED;
        }
        // Начальное направление скорости тоже случайно,
        // угол в пределах от 0 до 2PI
        double angle = Math.random()*2*Math.PI;
        // Вычисляются горизонтальная и вертикальная компоненты скорости
        speedX = 3*Math.cos(angle);
        speedY = 3*Math.sin(angle);

        // Цвет мяча выбирается случайно
        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        // Начальное положение мяча случайно
        x = Math.random()*(field.getSize().getWidth()-2*radius) + radius;
        y = Math.random()*(field.getSize().getHeight()-2*radius) + radius;
        // Создаем новый экземпляр потока, передавая аргументом
        // ссылку на класс, реализующий Runnable (т.е. на себя)
        Thread thisThread = new Thread(this);
        // Запускаем поток
        thisThread.start();
    }

    // Функции get и set
    public void setX(double x) { this.x = x; }

    public void setY(double y) { this.y = y; }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public boolean isSuperflag() { return superflag; }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public double getX() {
        return x;
    }

    public double getY() { return y; }

    public double getRadius(){ return radius; }


    // Делает мячик суперсильным
    public BouncingBall superball()
    {
        superflag=true;
        radius*=2;
        return this;
    }

    // Метод run() исполняется внутри потока. Когда он завершает работу,
    // то завершается и поток
    public void run() {
        try {
            // Крутим бесконечный цикл, т.е. пока нас не прервут,
            // мы не намерены завершаться
            while(true) {
                // Синхронизация потоков на самом объекте поля
                // Если движение разрешено - управление будет
                // возвращено в метод
                // В противном случае - активный поток заснет
                field.canMove(this);

                // проверка на столкновение с кирпичиками
                for(int i=0;i<field.getArrayH();i++)
                    for(int j=0;j<field.getArrayW();j++)
                    {
                        if(field.getArray_kirp()[i][j]!=null) {
                            if ((y + speedY >= field.getArray_kirp()[i][j].getY()) && (y + speedY <= field.getArray_kirp()[i][j].getWidth() + field.getArray_kirp()[i][j].getY()) && x + speedX >= field.getArray_kirp()[i][j].getX() + field.getArray_kirp()[i][j].getLength()) {
                                if ((x + speedX <= field.getArray_kirp()[i][j].getX() + field.getArray_kirp()[i][j].getLength() + radius)) {
                                    // Достигли левой плоскотси прямоугольника, отталкиваемся вправо
                                    speedX = -speedX;
                                    x = field.getArray_kirp()[i][j].getX() + field.getArray_kirp()[i][j].getLength() + radius;
                                    if(!superflag)
                                        field.getArray_kirp()[i][j].flagNow--;
                                    else
                                        field.getArray_kirp()[i][j].flagNow-= field.getArray_kirp()[i][j].getProchnost();
                                    if(field.getArray_kirp()[i][j].flagNow<=0) {
                                        field.deletekirp(i, j);
                                    }
                                }
                            }
                            else if((y + speedY >= field.getArray_kirp()[i][j].getY()) && (y+ speedY <= field.getArray_kirp()[i][j].getWidth()+field.getArray_kirp()[i][j].getY()) && (x+speedX <= field.getArray_kirp()[i][j].getX())) {
                                if ((x + speedX >= field.getArray_kirp()[i][j].getX() - radius)) {
                                    // Достигли правой плоскотси прямоугольника, отскакиваемся влево
                                    speedX = -speedX;
                                    x = field.getArray_kirp()[i][j].getX() - radius;
                                    if(!superflag)
                                        field.getArray_kirp()[i][j].flagNow--;
                                    else
                                        field.getArray_kirp()[i][j].flagNow-= field.getArray_kirp()[i][j].getProchnost();
                                    if(field.getArray_kirp()[i][j].flagNow<=0) {
                                        field.deletekirp(i, j);
                                    }
                                }
                            }
                            else if((x+speedX >= field.getArray_kirp()[i][j].getX()) && (x+speedX <= field.getArray_kirp()[i][j].getLength()+field.getArray_kirp()[i][j].getX()) && (y+speedY <= field.getArray_kirp()[i][j].getY())) {
                                if (y + speedY >= field.getArray_kirp()[i][j].getY() - radius) {
                                    // Достигли верхней плоскотси прямоугольника, отскакиваемся вверх
                                    speedY = -speedY;
                                    y = field.getArray_kirp()[i][j].getY() - radius;
                                    if(!superflag)
                                        field.getArray_kirp()[i][j].flagNow--;
                                    else
                                        field.getArray_kirp()[i][j].flagNow-= field.getArray_kirp()[i][j].getProchnost();
                                    if(field.getArray_kirp()[i][j].flagNow<=0) {
                                        field.deletekirp(i, j);
                                    }
                                }
                            }
                            else if((x+speedX >= field.getArray_kirp()[i][j].getX()) && (x+speedX <= field.getArray_kirp()[i][j].getLength()+field.getArray_kirp()[i][j].getX()) && (y+speedY >= field.getArray_kirp()[i][j].getY()+field.getArray_kirp()[i][j].getWidth())) {
                                if ((y + speedY <= field.getArray_kirp()[i][j].getY() + field.getArray_kirp()[i][j].getWidth() + radius)) {
                                    // Достигли нижней плоскотси прямоугольника, отскакиваемся вниз
                                    speedY = -speedY;
                                    y = field.getArray_kirp()[i][j].getY() + field.getArray_kirp()[i][j].getWidth() + radius;
                                    if(!superflag)
                                        field.getArray_kirp()[i][j].flagNow--;
                                    else
                                        field.getArray_kirp()[i][j].flagNow-= field.getArray_kirp()[i][j].getProchnost();
                                    if(field.getArray_kirp()[i][j].flagNow<=0) {
                                        field.deletekirp(i, j);
                                    }
                                }
                            }

                        }
                    }

                // Проверка на столкновение со стенками
                if (x + speedX <= radius) {
                    // Достигли левой стенки, отскакиваем право
                    speedX = -speedX;
                    x = radius;
                }
                else if (x + speedX >= field.getWidth() - radius) {
                    // Достигли правой стенки, отскок влево
                    speedX = -speedX;
                    x=new Double(field.getWidth()-radius).intValue();
                }
                else if (y + speedY <= radius) {
                    // Достигли верхней стенки
                    speedY = -speedY;
                    y = radius;
                }
                else if (y + speedY >= field.getHeight() - radius) {
                    // Достигли нижней стенки
                    speedY = -speedY;
                    y=new Double(field.getHeight()-radius).intValue();
                }

                else {
                    // Просто смещаемся
                    x += speedX;
                    y += speedY;
                }


                // Засыпаем на X миллисекунд, где X определяется
                // исходя из скорости
                // Скорость = 1 (медленно), засыпаем на 15 мс.
                // Скорость = 15 (быстро), засыпаем на 1 мс.
                Thread.sleep(16-speed);
            }
        } catch (InterruptedException ex) {
            // Если нас прервали, то ничего не делаем
            // и просто выходим (завершаемся)
        }
    }

    // Метод прорисовки самого себя
    public void paint(Graphics2D canvas) {
        if(!superflag) {
            canvas.setColor(color);
            canvas.setPaint(color);
        }
        else {
            canvas.setColor(color);
            canvas.setPaint(Color.RED);

        }
        Ellipse2D.Double ball = new Ellipse2D.Double(x-radius, y-radius, 2*radius, 2*radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }
}