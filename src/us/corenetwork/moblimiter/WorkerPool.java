package us.corenetwork.moblimiter;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class WorkerPool extends Thread
{
	private Queue<Runnable> tasks = new LinkedBlockingQueue<Runnable>();

	public void addTask(Runnable task)
	{
		tasks.offer(task);
	}

	@Override
	public void run()
	{
		while (true)
		{
			Runnable task = tasks.poll();
			if (task != null)
			{
				try
				{
					task.run();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			try
			{
				Thread.sleep(20);
			}
			catch (InterruptedException e)
			{
				break;
			}
		}
	}
}
