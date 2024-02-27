import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TeachingStaffUI extends JFrame implements ListSelectionListener,ActionListener {
	JTextField name;

	JList<Course> courseList;
	DefaultListModel<Course> courseModel;

	JList<String> gradeList;
	DefaultListModel<String> gradeModel;

	JButton validate,refresh;

	TeachingStaff staff;
    public TeachingStaffUI(TeachingStaff staff) {
        super(staff.getClass().toString().substring(6)+": "+staff.getName());

		this.staff=staff;
        setMinimumSize(new Dimension(600, 300));

		JPanel leftColumn=new JPanel(new BorderLayout());
		leftColumn.add(new JLabel("Courses:"),BorderLayout.NORTH);

		refresh=new JButton("Refresh Courses");
        refresh.addActionListener(this);

		leftColumn.add(refresh,BorderLayout.SOUTH);
		
		courseModel=new DefaultListModel<Course>();
		for(Course course:Catalog.getInstance().getCourses())
			if(course.isInCourse(staff))
				courseModel.addElement(course);
		
		courseList=new JList<Course>(courseModel);
		courseList.addListSelectionListener(this);
		JScrollPane courseSP=new JScrollPane(courseList);

		leftColumn.add(courseSP,BorderLayout.CENTER);

		Dimension d=new Dimension(200,20);
		name=myTextField(d);

		JPanel rightColumn=new JPanel(new BorderLayout());

        JPanel simpleFields=new JPanel(new GridLayout(2,1));
        JPanel nameLabel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameLabel.add(new JLabel("Course Name"));
        nameLabel.add(name);

		simpleFields.add(nameLabel);

		rightColumn.add(simpleFields,BorderLayout.NORTH);

		gradeModel=new DefaultListModel<String>();
		gradeList=new JList<String>(gradeModel);
		JScrollPane gradeSP=new JScrollPane(gradeList);

		JPanel gradeJP=new JPanel(new BorderLayout());
		gradeJP.add(new JLabel("Grades to be validated in this course:"),BorderLayout.NORTH);
		gradeJP.add(gradeSP,BorderLayout.CENTER);

		rightColumn.add(gradeJP,BorderLayout.CENTER);

		validate=new JButton("Validate Grades");
        validate.addActionListener(this);

		rightColumn.add(validate,BorderLayout.SOUTH);

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
		gradeModel.clear();
		name.setText(null);
		if(courseList.isSelectionEmpty())
            return;
        Course course=courseModel.get(courseList.getSelectedIndex());
		name.setText(course.getName());
		java.util.List<String> grades=Catalog.getInstance().getScoreVisitor().getUnverifiedGrades(staff,course);
		if(grades!=null)
			gradeModel.addAll(grades);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==validate)
		{
			staff.accept(Catalog.getInstance().getScoreVisitor());
			valueChanged(null);
		}
		else
		{
			courseModel.clear();
			for(Course course:Catalog.getInstance().getCourses())
				if(course.isInCourse(staff))
					courseModel.addElement(course);
			courseList.clearSelection();
			valueChanged(null);
		}
	}
}