package com.chestnut.RouterArchitecture.ModulesCommon;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.LinkedList;

/**
 * <p>
 * �ļ����� ��RecordPlayActivity.java
 * <p>
 * ����ժҪ ��
 * <p>
 * ���� ���⳽�� ����ʱ�� ��2012-7-25 ����01:35:48 ���� :��¼���߲��ŵ�����
 */
public class RecordPlayActivity extends Activity implements OnClickListener
{

	private static final String TAG = "RecordPlayActivity";
	/**
	 * ��ť
	 */
	private Button bt_exit;
	/**
	 * AudioRecord д�뻺������С
	 */
	protected int m_in_buf_size;
	/**
	 * ¼����Ƶ����
	 */
	private AudioRecord m_in_rec;
	/**
	 * ¼����ֽ�����
	 */
	private byte[] m_in_bytes;
	/**
	 * ���¼���ֽ�����Ĵ�С
	 */
	private LinkedList<byte[]> m_in_q;
	/**
	 * AudioTrack ���Ż����С
	 */
	private int m_out_buf_size;
	/**
	 * ������Ƶ����
	 */
	private AudioTrack m_out_trk;
	/**
	 * ���ŵ��ֽ�����
	 */
	private byte[] m_out_bytes;
	/**
	 * ¼����Ƶ�߳�
	 */
	private Thread record;
	/**
	 * ������Ƶ�߳�
	 */
	private Thread play;
	private int recordtimes;
	private int playtimes;
	/**
	 * ���߳�ֹͣ�ı�־
	 */
	private boolean flag = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.setTitle("��Ƶ��·");
		
		Log.d("dfdfd", "333333333333");
		
		init();
		record = new Thread(new recordSound());
		//play = new Thread(new playRecord());
		// ����¼���߳�
		record.start();
		// ���������߳�
		//play.start();
	}

	private void init()
	{
		bt_exit = (Button) findViewById(R.id.bt_yinpinhuilu_testing_exit);
		Log.i(TAG, "bt_exit====" + bt_exit);
		
		bt_exit.setOnClickListener(this);
		// AudioRecord �õ�¼����С�������Ĵ�С
		m_in_buf_size = AudioRecord.getMinBufferSize(44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		// ʵ����������Ƶ����
		m_in_rec = new AudioRecord(MediaRecorder.AudioSource.MIC,44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, m_in_buf_size);
		// ʵ����һ���ֽ����飬����Ϊ��С�������ĳ���
		m_in_bytes = new byte[m_in_buf_size];
		// ʵ����һ��������������ֽ�����
		m_in_q = new LinkedList<byte[]>();

		// AudioTrack �õ�������С�������Ĵ�С
		m_out_buf_size = AudioTrack.getMinBufferSize(44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		// ʵ����������Ƶ����
		m_out_trk = new AudioTrack(AudioManager.STREAM_MUSIC,44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, m_out_buf_size,
				AudioTrack.MODE_STREAM);
		// ʵ����һ������Ϊ������С�����С���ֽ�����
		m_out_bytes = new byte[m_out_buf_size];
		recordtimes = 0;
		playtimes = 0;
		Log.i(TAG, "........recordSound in_buf_size=" + m_in_buf_size);
		Log.i(TAG, "........recordSound out_buf_size=" + m_out_buf_size);
		
		
		
		
		
	}

	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{

		case R.id.bt_yinpinhuilu_testing_exit:
			flag = false;
			m_in_rec.stop();
			m_in_rec = null;
			m_out_trk.stop();
			m_out_trk = null;
			this.finish();
		}
	}

	/**
	 * <p>
	 * �ļ����� ��RecordPlayActivity.java
	 * <p>
	 * ����ժҪ ��
	 * <p>
	 * ���� ���⳽�� ����ʱ�� ��2012-7-25 ����01:57:09 ���� :¼���߳�
	 */
	class recordSound implements Runnable
	{
		@Override
		public void run()
		{
			Log.i(TAG, "........recordSound run()......");
			byte[] bytes_pkg;
			
			byte[] bytes_pkg1 = null;
			
			// ��ʼ¼��
			Log.i(TAG, "++++++++startRecording +++++++++");
			m_in_rec.startRecording();
			Log.i(TAG, "++++++++audioplay +++++++++");
			m_out_trk.play();
			while (flag)
			{
				Log.i(TAG, "++++++++startread +++++++++");
				m_in_rec.read(m_in_bytes, 0, m_in_buf_size);
				bytes_pkg = m_in_bytes.clone();
				//Log.i(TAG, "........recordSound bytes_pkg==" + bytes_pkg.length);
				//Log.i(TAG, "........recordtimes==" + recordtimes);
				//Log.i(TAG, "........(m_in_q.size==" + m_in_q.size());
				if (m_in_q.size() >= 2)
				{
					recordtimes++;
					m_in_q.removeFirst();
				}
				m_in_q.add(bytes_pkg);
				
				//m_out_trk.play();
				m_out_bytes = m_in_q.getFirst();
				bytes_pkg1 = m_out_bytes.clone();
				playtimes++;
				//Log.i(TAG, "........playRecord bytes_pkg1==" + bytes_pkg1.length);
				//Log.i(TAG, "........playtimes==" + playtimes);
				Log.i(TAG, "-------startwrite ---------");
				m_out_trk.write(bytes_pkg1, 0, bytes_pkg1.length);
			}
		}

	}

	/**
	 * <p>
	 * �ļ����� ��RecordPlayActivity.java
	 * <p>
	 * ����ժҪ ��
	 * <p>
	 * ���� ���⳽�� ����ʱ�� ��2012-7-25 ����01:57:34 ���� :�����߳�
	 */
	/*
	class playRecord implements Runnable
	{
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			Log.i(TAG, "........playRecord run()......");
			byte[] bytes_pkg = null;
			// ��ʼ����
			m_out_trk.play();

			while (flag)
			{
				try
				{
					m_out_bytes = m_in_q.getFirst();
					bytes_pkg = m_out_bytes.clone();
					playtimes++;
					Log.i(TAG, "........playRecord bytes_pkg==" + bytes_pkg.length);
					Log.i(TAG, "........playtimes==" + playtimes);
					m_out_trk.write(bytes_pkg, 0, bytes_pkg.length);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}*/

}