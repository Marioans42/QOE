package io;

import com.example.lib.AudioFormat;
import com.example.lib.AudioInputStream;
import com.example.lib.AudioSystem;
import com.example.lib.FloatSampleBuffer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;







public class MP3Decoder implements Decoder
{				
	AudioInputStream in;
	FloatSampleBuffer buffer;
	byte[] bytes;
	
	public MP3Decoder( InputStream stream ) throws Exception
	{
		InputStream in = new BufferedInputStream( stream, 1024*1024 );
		this.in = new MP3AudioFileReader( ).getAudioInputStream( in );
		AudioFormat baseFormat = this.in.getFormat();
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
											baseFormat.getSampleRate(), 16,
											baseFormat.getChannels(),
											baseFormat.getChannels() * 2,
											baseFormat.getSampleRate(), false);
		this.in = AudioSystem.getAudioInputStream(format, this.in);
	}

	@Override
	public int readSamples(float[] samples) 
	{
		if( buffer == null || buffer.getSampleCount() < samples.length )
		{
			buffer = new FloatSampleBuffer( in.getFormat().getChannels(), 1024, in.getFormat().getSampleRate() );
			bytes = new byte[buffer.getByteArrayBufferSize( in.getFormat() )];
		}
			
		int read = 0;			
		int readBytes = 0;
		try {
			readBytes = in.read( bytes, read, bytes.length - read );
		} catch (IOException e) {
			return 0;
		}
		if( readBytes == -1 )
			return 0;
		
		read += readBytes;
		while( readBytes != -1 && read != bytes.length )
		{
			try {
				readBytes = in.read( bytes, read, bytes.length - read );
			} catch (IOException e) {
				return 0;
			}
			read += readBytes;
		}	
		
		int frameCount = bytes.length / in.getFormat().getFrameSize();
		buffer.setSamplesFromBytes(bytes, 0, in.getFormat(), 0, frameCount);
		
		for( int i = 0; i <buffer.getSampleCount(); i++ )
		{						
			if( buffer.getChannelCount() == 2 )
				samples[i] = (buffer.getChannel(0)[i] + buffer.getChannel(1)[i]) / 2;
			else
				samples[i] = buffer.getChannel(0)[i];
		}
		
		return buffer.getSampleCount();
	}


}
