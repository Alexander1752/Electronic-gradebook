import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ParentUI extends JFrame implements ListSelectionListener,ActionListener {
	JTextField name,teacher;

	JList<Course> courseList;
	DefaultListModel<Course> courseModel;

	JList<Notification> notificationList;
	DefaultListModel<Notification> notificationModel;

	JButton refresh,clear,subscribe,unsubscribe;
	Course selectedCourse;

	Parent parent;
    public ParentUI(Parent parent) {
        super("Parent: "+parent.getName());

		this.parent=parent;
		selectedCourse=null;
        setMinimumSize(new Dimension(600, 300));

		JPanel leftColumn=new JPanel(new BorderLayout());
		leftColumn.add(new JLabel("Courses:"),BorderLayout.NORTH);
		
		courseModel=new DefaultListModel<Course>();
		courseModel.addAll(Catalog.getInstance().getCourses());
		
		courseList=new JList<Course>(courseModel);
		courseList.addListSelectionListener(this);
		JScrollPane courseSP=new JScrollPane(courseList);

		leftColumn.add(courseSP,BorderLayout.CENTER);

		refresh=new JButton("Refresh");
        refresh.addActionListener(this);

		leftColumn.add(refresh,BorderLayout.SOUTH);

		Dimension d=new Dimension(200,20);
		name=myTextField(d);
		teacher=myTextField(d);

		JPanel rightColumn=new JPanel(new BorderLayout());

        JPanel simpleFields=new JPanel(new GridLayout(3,1));
        JPanel nameLabel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel teacherLabel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel subscriptionPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nameLabel.add(new JLabel("Course Name"));
        nameLabel.add(name);
        teacherLabel.add(new JLabel("Teacher"));
        teacherLabel.add(teacher);

		subscribe=new JButton("Subscribe");
		subscribe.setEnabled(false);
		subscribe.addActionListener(this);
		subscriptionPanel.add(subscribe);

		unsubscribe=new JButton("Unubscribe");
		unsubscribe.setEnabled(false);
		unsubscribe.addActionListener(this);
		subscriptionPanel.add(unsubscribe);

		simpleFields.add(nameLabel);
		simpleFields.add(teacherLabel);
		simpleFields.add(subscriptionPanel);
		rightColumn.add(simpleFields,BorderLayout.NORTH);

		notificationModel=new DefaultListModel<Notification>();
		notificationModel.addAll(parent.getNotifications());
		notificationList=new JList<Notification>(notificationModel);
		JScrollPane notificationSP=new JScrollPane(notificationList);

		JPanel notificationJP=new JPanel(new BorderLayout());
		notificationJP.add(new JLabel("Notifications:"),BorderLayout.NORTH);
		notificationJP.add(notificationSP,BorderLayout.CENTER);

		rightColumn.add(notificationJP,BorderLayout.CENTER);

		clear=new JButton("Clear Notifications");
        clear.addActionListener(this);

		rightColumn.add(clear,BorderLayout.SOUTH);

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
		name.setText(null);
        teacher.setText(null);

		if(courseList.isSelectionEmpty())
            return;
        selectedCourse=courseModel.get(courseList.getSelectedIndex());
        name.setText(selectedCourse.getName());
        teacher.setText(selectedCourse.getTeacher().getName());
		if(selectedCourse.isParentSubscribed(parent))
		{
			subscribe.setEnabled(false);
			unsubscribe.setEnabled(true);
		}
		else
		{
			subscribe.setEnabled(true);
			unsubscribe.setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==refresh)
		{
			courseModel.clear();
			courseModel.addAll(Catalog.getInstance().getCourses());
			courseList.clearSelection();
			subscribe.setEnabled(false);
			unsubscribe.setEnabled(false);
			notificationModel.clear();
			notificationModel.addAll(parent.getNotifications());
			selectedCourse=null;
			valueChanged(null);
		}
		else if(e.getSource()==clear)
		{
			int ret=JOptionPane.showConfirmDialog(this,"Are you sure you want to delete ALL notifications?","Confirm notification clearing",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if(ret==JOptionPane.YES_OPTION)
			{
				parent.clearNotifications();
				notificationModel.clear();
			}
		}
		else if(e.getSource()==subscribe)
		{
			selectedCourse.addObserver(parent);
			subscribe.setEnabled(false);
			unsubscribe.setEnabled(true);
		}
		else if(e.getSource()==unsubscribe)
		{
			selectedCourse.removeObserver(parent);
			subscribe.setEnabled(true);
			unsubscribe.setEnabled(false);
		}
	}
}