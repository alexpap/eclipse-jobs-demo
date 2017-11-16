package demoeclipsejobs;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IProgressConstants;

public class View extends ViewPart {

	public static final String ID = "DemoEclipseJobs.view";
	
	public void setFocus() {}
	
	public void createPartControl(Composite parent) {
		
		// layout
		RowLayout layout = new RowLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
	 	parent.setLayout(layout);
		
	 	// synch button
		Button syncButton = new Button(parent, SWT.PUSH);
		syncButton.setText("Sync Job demo");
		syncButton.addListener(SWT.Selection, getSyncButtonListener());
		
		// async button
		Button asyncButton = new Button(parent, SWT.PUSH);
		asyncButton.setText("Async job demo");
		asyncButton.addListener(SWT.Selection, getAsyncButtonListener());
		
		// bug button
		Button bugButton = new Button(parent, SWT.PUSH);
		bugButton.setText("Bug job demo");
		bugButton.addListener(SWT.Selection, getBugButtonListener());
		
	}

	private Listener getBugButtonListener() {
		return new Listener(){
			
			public void handleEvent(Event event) {
	
				getBugJob().schedule();
			}		
		};
	}
	

	private Listener getSyncButtonListener() {
		return new Listener(){
			
			public void handleEvent(Event event) {
	
				getSynchJob().schedule();
			}

		};
	}

	private Listener getAsyncButtonListener() {
		
		return new Listener(){
			
			public void handleEvent(Event event) {
	
				getASynchJob().schedule();
			}

		};
	}

	private Job getSynchJob() {
		return new Job("Demo async job"){

			protected IStatus run(IProgressMonitor monitor) {
		        monitor.beginTask("Hello World (from a background a async job)", 100);
		        try {
					Thread.sleep(4*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return Status.OK_STATUS;
			}
			
		};
	}
	
	private Job getASynchJob() {
		
		return new Job("Demo sync job"){

			protected IStatus run(IProgressMonitor monitor) {
				
		        monitor.beginTask("Hello World (from a background async job)", 100);
		      
		        View.this.getThread(this).start();	
		        	
				return Job.ASYNC_FINISH;
			}
		};
	}

	private Thread getThread(final Job job) {
	
		return new Thread(){
    	
	    	@Override
			public void run() {
			
	    		try {
					Thread.sleep(4*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally{
					job.done(Status.OK_STATUS);
				}
			}
		};
	}
	
	private Job getBugJob() {
		
		final Job job = new Job("Bug Job"){

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				if (true) {
					setProperty(IProgressConstants.KEEP_PROPERTY, Boolean.TRUE);
					setProperty(
							IProgressConstants.ICON_PROPERTY,
							PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_DND_RIGHT_MASK));
					setProperty(IProgressConstants.ACTION_PROPERTY, new Action("test") {});
					return Status.OK_STATUS;
				}
				return Status.OK_STATUS;
			}
			
		};
		job.setUser(true);
		job.setPriority(Job.SHORT);
		return job;
	}
}