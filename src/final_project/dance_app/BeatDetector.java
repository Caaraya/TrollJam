package final_project.dance_app;

import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;

public class BeatDetector {
	private static final String TAG = "test";
	
	private Visualizer visualize = null;
	
	private double runningSoundAvg[];
	private double currentAvgEnergyOneSec[];
	private int numberOfSamplesInOneSec;
	private long systemTimeStartSec;
	
	private static final int LOW_FREQUENCY = 300;
	private static final int MIDLOW_FREQUENCY = 2000;
	private static final int MIDHIGH_FREQUENCY = 5000;
	private static final int HIGH_FREQUENCY = 10000;
	private final int high = 4;
	private final int midLow = 2;
	private final int midHigh = 3;
	private final int low = 1;
	public int current = 0;
	private OnBeatDetectedListener onBeatDetectedListener = null;

	public BeatDetector() {
	    init();
	}
	private void init() {
	    runningSoundAvg = new double[4];
	    currentAvgEnergyOneSec = new double[4];
	    currentAvgEnergyOneSec[0] = -1;
	    currentAvgEnergyOneSec[1] = -1;
	    currentAvgEnergyOneSec[2] = -1;
	    currentAvgEnergyOneSec[3] = -1;
	}
	
	public void link(MediaPlayer player)
	{
		if(player == null)
		{
			throw new NullPointerException("Cannot link to null MediaPlayer");
		}
		visualize = new Visualizer(player.getAudioSessionId());
		visualize.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		
		Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener()
		{
			@Override
	        public void onWaveFormDataCapture(Visualizer visualizer,
	                byte[] bytes, int samplingRate) {
	            // DO NOTHING
	        }
			@Override
	        public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
	                int samplingRate) {
	            updateVisualizerFFT(bytes);
	        }
	    };
	    visualize.setDataCaptureListener(captureListener,
	            Visualizer.getMaxCaptureRate() / 2, false, true);
	    visualize.setEnabled(true);
	    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	        @Override
	        public void onCompletion(MediaPlayer mediaPlayer) {
	            visualize.setEnabled(false);
	        }
	    });
	    systemTimeStartSec = System.currentTimeMillis();
	}
	
	public void release() {
	    if (visualize != null) {
	        visualize.setEnabled(false);
	        visualize.release();
	    }
	}

	public void pause() {
	    if (visualize != null) {
	        visualize.setEnabled(false);
	    }
	}

	public void resume() {
	    if (visualize != null) {
	        visualize.setEnabled(true);
	    }
	}
	
	
	public void updateVisualizerFFT(byte[] audioBytes) {
	    int energySum = 0;
	    energySum += Math.abs(audioBytes[0]);
	    int k = 2;
	    double captureSize = visualize.getCaptureSize() / 2;
	    int sampleRate = visualize.getSamplingRate() / 2000;
	    double nextFrequency = ((k / 2) * sampleRate) / (captureSize);
	    while (nextFrequency < LOW_FREQUENCY) {
	        energySum += Math.sqrt((audioBytes[k] * audioBytes[k])
	                * (audioBytes[k + 1] * audioBytes[k + 1]));
	        k += 2;
	        nextFrequency = ((k / 2) * sampleRate) / (captureSize);
	    }
	    double sampleAvgAudioEnergy = (double) energySum
	            / (double) ((k * 1.0) / 2.0);

	    runningSoundAvg[0] += sampleAvgAudioEnergy;
	    if ((sampleAvgAudioEnergy > currentAvgEnergyOneSec[0])
	            && (currentAvgEnergyOneSec[0] > 0)) {
	        current = low;
	    }
	    energySum = 0;
	    while (nextFrequency < MIDLOW_FREQUENCY) {
	        energySum += Math.sqrt((audioBytes[k] * audioBytes[k])
	                * (audioBytes[k + 1] * audioBytes[k + 1]));
	        k += 2;
	        nextFrequency = ((k / 2) * sampleRate) / (captureSize);
	    }

	    sampleAvgAudioEnergy = (double) energySum / (double) ((k * 1.0) / 2.0);
	    runningSoundAvg[1] += sampleAvgAudioEnergy;
	    if ((sampleAvgAudioEnergy > currentAvgEnergyOneSec[1])
	            && (currentAvgEnergyOneSec[1] > 0)) {
	        current = midLow;
	    }
	    energySum = Math.abs(audioBytes[1]);
	    while (nextFrequency < MIDHIGH_FREQUENCY) {
	        energySum += Math.sqrt((audioBytes[k] * audioBytes[k])
	                * (audioBytes[k + 1] * audioBytes[k + 1]));
	        k += 2;
	        nextFrequency = ((k / 2) * sampleRate) / (captureSize);
	    }

	    sampleAvgAudioEnergy = (double) energySum / (double) ((k * 1.0) / 2.0);
	    runningSoundAvg[2] += sampleAvgAudioEnergy;
	    if ((sampleAvgAudioEnergy > currentAvgEnergyOneSec[2])
	            && (currentAvgEnergyOneSec[2] > 0)) {
	       current = midHigh;
	    }
	    energySum = Math.abs(audioBytes[2]);


	    while ((nextFrequency < HIGH_FREQUENCY) && (k < audioBytes.length)) {
	        energySum += Math.sqrt((audioBytes[k] * audioBytes[k])
	                * (audioBytes[k + 1] * audioBytes[k + 1]));
	        k += 2;
	        nextFrequency = ((k / 2) * sampleRate) / (captureSize);
	    }

	    sampleAvgAudioEnergy = (double) energySum / (double) ((k * 1.0) / 2.0);
	    runningSoundAvg[3] += sampleAvgAudioEnergy;
	    if ((sampleAvgAudioEnergy > currentAvgEnergyOneSec[3])
	            && (currentAvgEnergyOneSec[3] > 0)) {
	       current = high;
	    }

	    numberOfSamplesInOneSec++;
	    if ((System.currentTimeMillis() - systemTimeStartSec) > 1000) {
	        currentAvgEnergyOneSec[0] = runningSoundAvg[0]
	                / numberOfSamplesInOneSec;
	        currentAvgEnergyOneSec[1] = runningSoundAvg[1]
	                / numberOfSamplesInOneSec;
	        currentAvgEnergyOneSec[2] = runningSoundAvg[2]
	                / numberOfSamplesInOneSec;
	        currentAvgEnergyOneSec[3] = runningSoundAvg[3]
	                / numberOfSamplesInOneSec;
	        numberOfSamplesInOneSec = 0;
	        runningSoundAvg[0] = 0.0;
	        runningSoundAvg[1] = 0.0;
	        runningSoundAvg[2] = 0.0;
	        runningSoundAvg[3] = 0.0;
	        systemTimeStartSec = System.currentTimeMillis();
	    }
	}
	
	public int setDanceMove()
	{
		return current;
	}
	


// marker	
	public void setOnBeatDetectedListener(OnBeatDetectedListener listener) {
	    onBeatDetectedListener = listener;
	}

	public interface OnBeatDetectedListener {
	    public abstract void onBeatDetectedLow();

	    public abstract void onBeatDetectedMidLow();
	    
	    public abstract void onBeatDetectedMidHigh();

	    public abstract void onBeatDetectedHigh();
	}
}
