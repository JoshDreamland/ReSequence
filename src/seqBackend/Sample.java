package seqBackend;

/**
 * A Sample; sort of an instrument which can be rendered at a certain bit rate for a given note.
 */
public abstract class Sample
{
	/**
	 * @param duration The length of time the sample should be rendered in
	 * @param sps The sample rate, in samples per second.
	 * @param note The note that should be played.
	 * @param endnote The note that should be faded to at the end of this sample.
	 * @return A byte array containing the wave data.
	 */
	abstract byte[] render(int duration, int sps, Note note, Note endnote);
}
