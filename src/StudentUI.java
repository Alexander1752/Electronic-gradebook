import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StudentUI extends JFrame implements ListSelectionListener,ActionListener {
	JTextField name,teacher,studentAssistant,partial,exam;

	JList<Course> courseList;
	DefaultListModel<Course> courseModel;

	JList<Assistant> assistantList;
	DefaultListModel<Assistant> assistantModel;

	JButton refresh;

	Student student;
    public StudentUI(Student student) {
        super("Student: "+student.getName());

		this.student=student;
        setMinimumSize(new Dimension(600, 300));

		JPanel leftColumn=new JPanel(new BorderLayout());
		leftColumn.add(new JLabel("Courses:"),BorderLayout.NORTH);
		
		courseModel=new DefaultListModel<Course>();
		for(Course course:Catalog.getInstance().getCourses())
			if(course.getAllStudents().contains(student))
			courseModel.addElement(course);
		
		courseList=new JList<Course>(courseModel);
		courseList.addListSelectionListener(this);
		JScrollPane courseSP=new JScrollPane(courseList);

		leftColumn.add(courseSP,BorderLayout.CENTER);

		refresh=new JButton("Refresh Courses");
        refresh.addActionListener(this);

		leftColumn.add(refresh,BorderLayout.SOUTH);

		Dimension d=new Dimension(200,20);
		name=myTextField(d);
		teacher=myTextField(d);
		studentAssistant=myTextField(d);
		partial=myTextField(d);
		exam=myTextField(d);

		JPanel rightColumn=new JPanel(new BorderLayout());

        JPanel simpleFields=new JPanel(new GridLayout(6,1));
        JPanel nameLabel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel teacherLabel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel studentAssistantLabel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel partialLabel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel examLabel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nameLabel.add(new JLabel("Course Name"));
        nameLabel.add(name);
        teacherLabel.add(new JLabel("Teacher"));
        teacherLabel.add(teacher);
		studentAssistantLabel.add(new JLabel("Assistant"));
        studentAssistantLabel.add(studentAssistant);
		partialLabel.add(new JLabel("Partial Grade"));
        partialLabel.add(partial);
		examLabel.add(new JLabel("Exam Grade"));
        examLabel.add(exam);

		simpleFields.add(nameLabel);
		simpleFields.add(teacherLabel);
		simpleFields.add(studentAssistantLabel);
		simpleFields.add(partialLabel);
		simpleFields.add(examLabel);

		rightColumn.add(simpleFields,BorderLayout.NORTH);

		assistantModel=new DefaultListModel<Assistant>();
		assistantList=new JList<Assistant>(assistantModel);
		JScrollPane assistantSP=new JScrollPane(assistantList);

		JPanel assistantJP=new JPanel(new BorderLayout());
		assistantJP.add(new JLabel("Assistants of this course:"),BorderLayout.NORTH);
		assistantJP.add(assistantSP,BorderLayout.CENTER);

		rightColumn.add(assistantJP,BorderLayout.CENTER);
		rightColumn.setMinimumSize(new Dimension(350,300));

		JSplitPane mainSP=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftColumn,rightColumn);
		mainSP.setResizeWeight(0.4);
		add(mainSP,BorderLayout.CENTER);

		pack();
        setVisible(true);
    }
    
	private static JTextField myTextField(Dimension d)
	{
		JTextField ret =new JTextField();
        ret.setPreferredSize(d);
        ret.setEditable(false);
		return ret;
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		assistantModel.clear();
		name.setText(null);
        teacher.setText(null);
		studentAssistant.setText(null);
		exam.setText(null);
		partial.setText(null);

		if(courseList.isSelectionEmpty())
            return;
        Course course=courseModel.get(courseList.getSelectedIndex());
        name.setText(course.getName());
        teacher.setText(course.getTeacher().getName());
		for(Group group:course.getGroups())
			if(group.contains(student))
			{
				studentAssistant.setText(group.getAssistant().getName());
				break;
			}
		Grade grade=course.getGrade(student);
		if(grade==null)
		{
			exam.setText("-");
			partial.setText("-");
		}
		else
		{
			if(grade.getPartialScore()>0)
				partial.setText(grade.getPartialScore().toString());
			else partial.setText("-");

			if(grade.getExamScore()>0)
				exam.setText(grade.getExamScore().toString());
			else exam.setText("-");
		}
		for(Object o:course.getAssistants())
			if(o instanceof Assistant)
				assistantModel.addElement((Assistant)o);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		courseModel.clear();
		for(Course course:Catalog.getInstance().getCourses())
			if(course.getAllStudents().contains(student))
				courseModel.addElement(course);
		courseList.clearSelection();
		valueChanged(null);
	}
}