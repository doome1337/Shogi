package mgci.jhdap.shogi;

/** The abstract Class used for dealing with 
 * text commands via polymorphism.
 */
public abstract class Command
{
	public final String regex;
	public final String detail;
			
	public Command (String regex, String detail)
	{
		this.regex = regex;
		this.detail = detail;
	}
	
	abstract void execute (String command);	
}