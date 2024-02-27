import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GraphicalInterface extends JFrame implements ActionListener {
    JButton studentButton,teacherButton,parentButton,assistantButton;
    JTextField path;
    JTextArea text;
    
    public GraphicalInterface(String winName) {
        super(winName);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(300, 300));

        JPanel buttons=new JPanel(new GridLayout(2,2));

        studentButton=new JButton("Student");
        studentButton.setEnabled(true);
        studentButton.addActionListener(this);
		studentButton.setMaximumSize(new Dimension(20,20));
        buttons.add(studentButton);

		parentButton=new JButton("Parent");
        parentButton.setEnabled(true);
        parentButton.addActionListener(this);
        buttons.add(parentButton);

		teacherButton=new JButton("Teacher");
        teacherButton.setEnabled(true);
        teacherButton.addActionListener(this);
        buttons.add(teacherButton);

		assistantButton=new JButton("Assitant");
        assistantButton.setEnabled(true);
        assistantButton.addActionListener(this);
        buttons.add(assistantButton);

        add(new JLabel(new ImageIcon("catalog.png")),BorderLayout.NORTH);

		add(buttons,BorderLayout.CENTER);

        setSize(300, 300);
		pack();
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
		String name=JOptionPane.showInputDialog(this,"Give the first name and the last name of the user:");
		if(name==null)
			return;
        if(e.getSource()==studentButton)
		{
			Student student=null; 
			for(Course course:Catalog.getInstance().getCourses())
			{
				for(Student temp:course.getAllStudents())
					if(temp.isNamed(name))
					{
						student=temp;
						break;
					}
				if(student!=null)
					break;
			}
			if(student==null)
				JOptionPane.showMessageDialog(this, "Student named \""+name+"\" not found", "Not Found", JOptionPane.ERROR_MESSAGE);
			else 
				new StudentUI(student);
		}
		else if(e.getSource()==parentButton)
		{
			System.out.println("Parent pressed");
            Parent parent=null;
            search:
            {
                for(Course course:Catalog.getInstance().getCourses())
                    for(Student temp:course.getAllStudents())
                        if(temp.getFather()!=null&&temp.getFather().isNamed(name))
                        {
                            parent=temp.getFather();
                            break search;
                        }
                        else if(temp.getMother()!=null&&temp.getMother().isNamed(name))
                        {
                            parent=temp.getMother();
                            break search;
                        }
            }
            if(parent==null)
				JOptionPane.showMessageDialog(this, "Parent named \""+name+"\" not found", "Not Found", JOptionPane.ERROR_MESSAGE);
            else 
				new ParentUI(parent);
		}
		else if(e.getSource()==teacherButton)
		{
			System.out.println("Teacher pressed");
			Teacher teacher=null;
			for(Course course:Catalog.getInstance().getCourses())
				if(course.getTeacher().isNamed(name))
				{
					teacher=course.getTeacher();
					break;
				}
			if(teacher==null)
				JOptionPane.showMessageDialog(this, "Teacher named \""+name+"\" not found", "Not Found", JOptionPane.ERROR_MESSAGE);
            else 
				new TeachingStaffUI(teacher);
		}
		else if(e.getSource()==assistantButton)
		{
			System.out.println("Assistant pressed");
            Assistant assistant=null;
            search:
            {
                for(Course course:Catalog.getInstance().getCourses())
                    for(Assistant temp:course.getAssistants())
                        if(temp.isNamed(name))
                        {
                            assistant=temp;
                            break search;
                        }
            }
            if(assistant==null)
				JOptionPane.showMessageDialog(this, "Assistant named \""+name+"\" not found", "Not Found", JOptionPane.ERROR_MESSAGE);
            else 
				new TeachingStaffUI(assistant);
		}


		return;
    }
    
    public static void main(String args[]) {
        new GraphicalInterface("Catalog");
    }
}