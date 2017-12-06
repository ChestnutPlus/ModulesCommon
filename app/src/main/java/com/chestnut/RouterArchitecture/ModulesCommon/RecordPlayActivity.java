package com.chestnut.RouterArchitecture.ModulesCommon;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
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
	 * ¼����Ƶ�߳�
	 */
	private Thread record;
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
		init();
		record = new Thread(new recordSound());
		record.start();
	}

	private int sampleRateLnHz = 44100;

	private void init()
	{
		bt_exit = (Button) findViewById(R.id.bt_yinpinhuilu_testing_exit);
		bt_exit.setOnClickListener(this);
		m_in_buf_size = AudioRecord.getMinBufferSize(sampleRateLnHz,
				AudioFormat.CHANNEL_IN_STEREO,
				AudioFormat.ENCODING_PCM_16BIT) / 4;
		m_in_rec = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRateLnHz,
				AudioFormat.CHANNEL_IN_STEREO,
				AudioFormat.ENCODING_PCM_16BIT, m_in_buf_size);
		m_in_bytes = new byte[m_in_buf_size];

		m_out_buf_size = AudioTrack.getMinBufferSize(sampleRateLnHz,
				AudioFormat.CHANNEL_OUT_STEREO,
				AudioFormat.ENCODING_PCM_16BIT);
		//audioTrack播放的时候，会断断续续，
		//解决：buff扩大：http://blog.csdn.net/samguoyi/article/details/7410779
		m_out_trk = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRateLnHz,
				AudioFormat.CHANNEL_OUT_STEREO,
				AudioFormat.ENCODING_PCM_16BIT, m_out_buf_size*4,
				AudioTrack.MODE_STREAM);
	}

	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.bt_yinpinhuilu_testing_exit:
				release();
		}
	}

	public void release() {
		flag = false;
		m_in_rec.stop();
		m_in_rec.release();
		m_in_rec = null;
		m_out_trk.stop();
		m_out_trk.release();
		m_out_trk = null;
		this.finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		release();
	}

	class recordSound implements Runnable
	{
		@Override
		public void run()
		{
			m_in_rec.startRecording();
			m_out_trk.play();
			while (flag)
			{
				m_in_rec.read(m_in_bytes, 0, m_in_buf_size);
				m_out_trk.write(m_in_bytes, 0, m_in_bytes.length);
			}
		}
	}
}