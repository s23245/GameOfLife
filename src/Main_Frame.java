import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class Main_Frame  implements ActionListener
{
    JFrame frame;

    JPanel main_panel_frame;
    JPanel top_panel;
    JPanel numbers_grid;

    JCheckBox[] cb_life_number = new JCheckBox[9];
    JCheckBox[] cb_dead_number = new JCheckBox[9];

    int[] life_numbers = new int[9];
    int[] dead_numbers = new int[9];

    JPanel game_field;
    JPanel[][] game_field_boxes = new JPanel[40][40];
    int[][] game_field_numbers = new int[40][40];

    JPanel buttons_panel;
    JButton step_button;
    JButton restart_button;

    Color LIFE_COLOR  = new Color(173, 193, 120);


    public Main_Frame()
    {
        frame = new JFrame("GAME OF LIFE");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 1024);
        setVisible();

        main_panel_frame = new JPanel(new BorderLayout());
        main_panel_frame.setVisible(true);
        main_panel_frame.setSize(new Dimension(frame.getWidth(), frame.getHeight()));
        frame.add(main_panel_frame);

        top_panel = new JPanel(new BorderLayout());
        top_panel.setSize(new Dimension(main_panel_frame.getWidth(), main_panel_frame.getHeight()/10));
        top_panel.setVisible(true);
        top_panel.setBackground(Color.GRAY);

        numbers_grid = new JPanel(new GridLayout(2, 10,10,10));
        numbers_grid.setVisible(true);
        numbers_grid.setBackground(new Color(33, 158, 188));
        numbers_grid.setPreferredSize(new Dimension((int)(top_panel.getWidth()*0.80), top_panel.getHeight()));
        numbers_grid.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        for(int j = 0 ; j < 2; j++)
        {
            for(int i = 0; i <= 8; i++)
            {
                if(j == 0)
                {
                    cb_life_number[i] = new JCheckBox(""+i);
                    cb_life_number[i].setFont(new Font("Verdana", Font.BOLD, 18));
                    cb_life_number[i].setHorizontalAlignment(SwingConstants.CENTER);
                    numbers_grid.add(cb_life_number[i]);

                }
                else
                {
                    cb_dead_number[i] = new JCheckBox(""+i);
                    cb_dead_number[i].setFont(new Font("Verdana", Font.BOLD, 18));
                    cb_dead_number[i].setHorizontalAlignment(SwingConstants.CENTER);
                    numbers_grid.add(cb_dead_number[i]);
                }

            }
        }


        buttons_panel = new JPanel(new BorderLayout());
        buttons_panel.setPreferredSize(new Dimension((int)(top_panel.getWidth()*0.20), top_panel.getHeight()));
        buttons_panel.setVisible(true);


        step_button = new JButton("NEXT STEP");
        step_button.setHorizontalAlignment(SwingConstants.CENTER);
        step_button.setSize(new Dimension(buttons_panel.getWidth()/2, buttons_panel.getHeight()));
        step_button.setFont(new Font("Verdana", Font.BOLD, 18));
        step_button.setForeground(Color.white);
        step_button.setBorderPainted(false);
        step_button.setOpaque(false);
        step_button.setContentAreaFilled(false);
        step_button.addActionListener(this);
        step_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                step_button.setBackground(new Color(2, 28, 51));
                step_button.setOpaque(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                step_button.setOpaque(false);
            }
        });
        buttons_panel.add(step_button,BorderLayout.WEST);

        restart_button = new JButton("RESTART");
        restart_button.setHorizontalAlignment(SwingConstants.CENTER);
        restart_button.setSize(new Dimension(buttons_panel.getWidth()/2, buttons_panel.getHeight()));
        restart_button.setFont(new Font("Verdana", Font.BOLD, 18));
        restart_button.setForeground(Color.white);
        restart_button.setBorderPainted(false);
        restart_button.setOpaque(false);
        restart_button.setContentAreaFilled(false);
        restart_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                restart_button.setBackground(new Color(2, 28, 51));
                restart_button.setOpaque(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                restart_button.setOpaque(false);
            }
        });
        buttons_panel.add(restart_button,BorderLayout.EAST);

        top_panel.add(buttons_panel,BorderLayout.WEST);
        top_panel.add(numbers_grid, BorderLayout.EAST);

        main_panel_frame.add(top_panel,BorderLayout.NORTH);



        game_field = new JPanel(new GridLayout(40,40));
        game_field.setVisible(true);
        game_field.setMaximumSize(new Dimension(main_panel_frame.getWidth(), main_panel_frame.getHeight()));
        game_field.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        for(int j = 0; j < 40; j++)
        {
            for(int i = 0; i < 40; i++)
            {

                game_field_boxes[j][i] = new JPanel();
                game_field_boxes[j][i].setBorder(BorderFactory.createLineBorder(Color.black));
                if((j == 20 && i == 20) || (j == 21 && i == 21))
                    game_field_boxes[j][i].setBackground(LIFE_COLOR);
                game_field.add(game_field_boxes[j][i]);
            }
        }
        main_panel_frame.add(game_field,BorderLayout.CENTER);

        frame.validate();

    }

    public static void main(String[] args)
    {
        try {
            javax.swing.UIManager.setLookAndFeel( "javax.swing.plaf.nimbus.NimbusLookAndFeel" );
        } catch( Exception e ) {
            e.printStackTrace();
        }
        new Main_Frame();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == step_button)
        {
            life_numbers = NumbersAreSelectedLife();
            dead_numbers = NumbersAreSelectedDead();

            game_field_numbers = FieldCheck();


            game_field_numbers = NextStepNumbers();


            FieldDraw();
        }

    }

    private void FieldDraw()
    {
        for(int i = 0; i < 40; i ++)
        {
            for (int j = 0; j < 40; j++)
            {
                if(game_field_numbers[j][i] == 1)
                    game_field_boxes[j][i].setBackground(LIFE_COLOR);
                else
                    game_field_boxes[j][i].setBackground(Color.WHITE);
            }
        }
    }

    private int[][] NextStepNumbers()
    {
        for(int j = 0; j < 40;j++)
        {
            for(int i = 0; i < 40; i++)
            {
                if(game_field_numbers[j][i] == 0)
                {
                     CheckNumbersDead(j,i);
                }
                else if (game_field_numbers[j][i] == 1)
                    CheckNumbersLife(j,i);
            }
        }


        return  ChangeNumbers();
    }

    private int[][] ChangeNumbers()
    {
        for(int i = 0; i < 40; i++)
        {
            for (int j = 0; j < 40; j++)
            {
                if(game_field_numbers[j][i] == -1)
                    game_field_numbers[j][i] = 1;
                else if(game_field_numbers[j][i] == -2)
                    game_field_numbers[j][i] = 0;
            }
        }
        return game_field_numbers;
    }

    private void CheckNumbersLife(int y, int x)
    {
        int sum_of_neighbors = 0;
        boolean isStillAlive = false;

        sum_of_neighbors = getSum_of_neighbors(y, x, sum_of_neighbors);

        for(int i = 0; i <= 8; i++)
        {

            if (sum_of_neighbors == life_numbers[i])
            {
                isStillAlive = true;
                break;
            }
        }

        if(isStillAlive == false)
            game_field_numbers[y][x] = -2;
    }

    private void CheckNumbersDead(int y,int x)
    {
        int sum_of_neighbors = 0;

        sum_of_neighbors = getSum_of_neighbors(y, x, sum_of_neighbors);

        for (int j = 0; j <= 8; j++)
        {

            if (sum_of_neighbors == dead_numbers[j])
            {
                game_field_numbers[y][x] = -1;
                break;
            }
        }

    }

    private int getSum_of_neighbors(int y, int x, int sum_of_neighbors)
    {
        if((y == 0 && x == 0) || (y == 0 && x == 39) || (y == 39 && x == 39) || (y == 39 && x == 0))
        {
           sum_of_neighbors =  CheckCorners(y,x,sum_of_neighbors);
        }
        else if(
                        ( y >= 1 && y < 39 && x == 0) ||     // left side
                        ( y == 0 && x >= 1 && x < 39) ||    // top side
                        ( y >= 1 && y < 39 && x == 39)||    // right side
                        ( y == 39 && x >= 1 && x < 39)      // bottom side
        )
        {
            sum_of_neighbors = CheckSides(y,x,sum_of_neighbors);
        }
        else
        {
            for (int j = y - 1; j <= y + 1; j++)
            {
                for (int t = x - 1; t <= x + 1; t++)
                {
                    if (j != y || t != x)
                        if (game_field_numbers[j][t] == 1 || game_field_numbers[j][t] == -2)
                            sum_of_neighbors++;
                }

            }
        }


        return sum_of_neighbors;
    }

    private int CheckSides(int y, int x, int sum_of_neighbors)
    {
        return  sum_of_neighbors;
    }

    private int CheckCorners(int y, int x, int sum_of_neighbors)
    {
       if(y == 0 && x == 0)
       {
           //top
          if(game_field_numbers[39][39] == 1 || game_field_numbers[39][39] == -2)
              sum_of_neighbors++;
           if(game_field_numbers[39][0] == 1 || game_field_numbers[39][0] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[39][1] == 1 || game_field_numbers[39][1] == -2)
               sum_of_neighbors++;
           //center
           if(game_field_numbers[0][39] == 1 || game_field_numbers[0][39] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[0][1] == 1 || game_field_numbers[0][1] == -2)
               sum_of_neighbors++;
           //bottom
           if(game_field_numbers[1][39] == 1 || game_field_numbers[1][39] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[1][0] == 1 || game_field_numbers[1][0] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[1][1] == 1 || game_field_numbers[1][1] == -2)
               sum_of_neighbors++;

       }
       else if (y == 0 && x == 39)
       {
           //top
           if(game_field_numbers[39][38] == 1 || game_field_numbers[39][38] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[39][39] == 1 || game_field_numbers[39][39] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[39][0] == 1 || game_field_numbers[39][0] == -2)
               sum_of_neighbors++;
           //center
           if(game_field_numbers[0][38] == 1 || game_field_numbers[0][38] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[0][0] == 1 || game_field_numbers[0][0] == -2)
               sum_of_neighbors++;
           //bottom
           if(game_field_numbers[1][38] == 1 || game_field_numbers[1][38] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[1][39] == 1 || game_field_numbers[1][39] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[1][0] == 1 || game_field_numbers[1][0] == -2)
               sum_of_neighbors++;

       }
       else if (y == 39 && x == 39)
       {
           //top
           if(game_field_numbers[38][38] == 1 || game_field_numbers[38][38] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[38][39] == 1 || game_field_numbers[38][39] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[38][0] == 1 || game_field_numbers[38][0] == -2)
               sum_of_neighbors++;
           //center
           if(game_field_numbers[39][38] == 1 || game_field_numbers[39][38] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[39][0] == 1 || game_field_numbers[39][0] == -2)
               sum_of_neighbors++;
           //bottom
           if(game_field_numbers[0][38] == 1 || game_field_numbers[0][38] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[0][39] == 1 || game_field_numbers[0][39] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[0][0] == 1 || game_field_numbers[0][0] == -2)
               sum_of_neighbors++;

       }
       else if (y == 39 && x == 0)
       {
           //top
           if(game_field_numbers[38][39] == 1 || game_field_numbers[38][39] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[38][0] == 1 || game_field_numbers[38][0] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[38][1] == 1 || game_field_numbers[38][1] == -2)
               sum_of_neighbors++;
           //center
           if(game_field_numbers[39][39] == 1 || game_field_numbers[39][39] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[39][1] == 1 || game_field_numbers[39][1] == -2)
               sum_of_neighbors++;
           //bottom
           if(game_field_numbers[0][39] == 1 || game_field_numbers[0][39] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[0][0] == 1 || game_field_numbers[0][0] == -2)
               sum_of_neighbors++;
           if(game_field_numbers[0][1] == 1 || game_field_numbers[0][1] == -2)
               sum_of_neighbors++;

       }

        return sum_of_neighbors;
    }

    private int[][] FieldCheck()
    {
        for(int j = 0; j < 40; j ++)
        {
            for(int i = 0; i < 40; i++)
            {
                if(game_field_boxes[j][i].getBackground() == LIFE_COLOR)
                {
                    game_field_numbers[j][i] = 1;
                }
                else
                    game_field_numbers[j][i] = 0;
            }
        }
        return game_field_numbers;
    }

    private int[] NumbersAreSelectedDead()
    {
        for(int i = 0; i <= 8; i ++)
        {
            dead_numbers[i] = -1;
            if(cb_dead_number[i].isSelected())
                dead_numbers[i] = i;
        }

        return dead_numbers;
    }

    private int[] NumbersAreSelectedLife()
    {
        for (int i = 0; i <= 8; i++)
        {
            life_numbers[i] = -1;
            if(cb_life_number[i].isSelected())
                life_numbers[i] = i;
        }
        return life_numbers;
    }

    public void setVisible(){frame.setVisible(true);}
}
