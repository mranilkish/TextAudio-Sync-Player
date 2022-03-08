
/*
 * @(#) Helpers.java
 *
 * Created on 08.02.2018 by Anil Kishan (quippy@quippy.de)
 *
 *-----------------------------------------------------------------------
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */
package sm.smcreator.smc.system;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.swing.JProgressBar;

import sm.smcreator.smc.main.gui.tools.FileChooserResult;
import sm.smcreator.smc.main.gui.tools.PlaylistDropListener;
import javax.crypto.Mac;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * @author Anil Kishan
 * @since 08.02.2018
 */
public class Helpers
{
    /**
	 * Constructor for Helpers, not used!
	 */
	private Helpers()
	{
		super();
	}

	/** Version Information */
	public static final String VERSION = "V1.0";
	public static final String PROGRAM = "SMCreator";
	public static final String FULLVERSION = PROGRAM+' '+VERSION;
	public static final String COPYRIGHT = " P Anil Kishan";
	
	public static final String VERSION_URL = "";
	public static final String JAVAMOD_URL = "";
	public static final String HOMEDIR = System.getProperty("user.home");

	/** Codepages used when reading mod files */
	public static final String CODING_GUI = "cp850";
	public static final String CODING_COMMANLINE = "cp1252";
	public static String currentCoding = CODING_GUI;
        public static String filename=null;
        public static String skk="";

	public static final Font DIALOG_FONT = new Font("Dialog", Font.PLAIN, 12);
    public static final Font TEXTAREA_FONT = getTextAreaFont();
	public static final String DEFAULTFONTPATH = "/sm/smcreator/smc/main/gui/ressources/lucon.ttf";

	/**
	 * It's rediculous, but with the GUI we need cp850 but with
	 * the commandline we need cp1252. Unbeleavable...
	 * @since 02.06.2013
	 * @param gui
	 */
	public static void setCoding(boolean gui)
	{
		currentCoding = (gui)?CODING_GUI:CODING_COMMANLINE;
	}
	public static Font getTextAreaFont()
	{
		try
		{
			InputStream is = Helpers.class.getResourceAsStream(Helpers.DEFAULTFONTPATH);
			Font font = Font.createFont(Font.TRUETYPE_FONT, is);
			return font.deriveFont(10.0f);
		}
		catch (Exception ex)
		{
			Log.error("Could not load font!", ex);
			return new Font("Courier", Font.PLAIN, 8);
		}
	}

	/**
	 * This is only used for translating periods into note names, for the
	 * patternElement.toString() method
	 */
	

	/**
	 * From the Protracker V2.1A Playroutine, added one Octave above and two at the end
	 * to avoid array index overflows (mainly with arpeggios)
	 */
	
	/**
	 * Basic C4 S3M/IT --> These Values are from above (Octave 0)!
	 */
	
	/**
	 * This table is used by the stm, s3m, it mods, manly for the
	 * old "fineTune" effekt as they are downward compatible
	 * calculated by 8363*2^((i-8)/(12*8))
	 */

	/**
	 * not yet used 
	 */
	

	/**
	 * not yet used
	 */
	

	/**
	 * Pitch Envelope Slider Table
	 */
	public static final int [] LinearSlideUpTable = new int[] 
	{
		65536, 65773, 66010, 66249, 66489, 66729, 66971, 67213, 
		67456, 67700, 67945, 68190, 68437, 68685, 68933, 69182, 
		69432, 69684, 69936, 70189, 70442, 70697, 70953, 71209, 
		71467, 71725, 71985, 72245, 72507, 72769, 73032, 73296, 
		73561, 73827, 74094, 74362, 74631, 74901, 75172, 75444, 
		75717, 75991, 76265, 76541, 76818, 77096, 77375, 77655, 
		77935, 78217, 78500, 78784, 79069, 79355, 79642, 79930, 
		80219, 80509, 80800, 81093, 81386, 81680, 81976, 82272, 
		82570, 82868, 83168, 83469, 83771, 84074, 84378, 84683, 
		84989, 85297, 85605, 85915, 86225, 86537, 86850, 87164, 
		87480, 87796, 88113, 88432, 88752, 89073, 89395, 89718, 
		90043, 90369, 90695, 91023, 91353, 91683, 92015, 92347, 
		92681, 93017, 93353, 93691, 94029, 94370, 94711, 95053, 
		95397, 95742, 96088, 96436, 96785, 97135, 97486, 97839, 
		98193, 98548, 98904, 99262, 99621, 99981, 100343, 100706, 
		101070, 101435, 101802, 102170, 102540, 102911, 103283, 103657, 
		104031, 104408, 104785, 105164, 105545, 105926, 106309, 106694, 
		107080, 107467, 107856, 108246, 108637, 109030, 109425, 109820, 
		110217, 110616, 111016, 111418, 111821, 112225, 112631, 113038, 
		113447, 113857, 114269, 114682, 115097, 115514, 115931, 116351, 
		116771, 117194, 117618, 118043, 118470, 118898, 119328, 119760, 
		120193, 120628, 121064, 121502, 121941, 122382, 122825, 123269, 
		123715, 124162, 124611, 125062, 125514, 125968, 126424, 126881, 
		127340, 127801, 128263, 128727, 129192, 129660, 130129, 130599, 
		131072, 131546, 132021, 132499, 132978, 133459, 133942, 134426, 
		134912, 135400, 135890, 136381, 136875, 137370, 137866, 138365, 
		138865, 139368, 139872, 140378, 140885, 141395, 141906, 142419, 
		142935, 143451, 143970, 144491, 145014, 145538, 146064, 146593, 
		147123, 147655, 148189, 148725, 149263, 149803, 150344, 150888, 
		151434, 151982, 152531, 153083, 153637, 154192, 154750, 155310, 
		155871, 156435, 157001, 157569, 158138, 158710, 159284, 159860, 
		160439, 161019, 161601, 162186, 162772, 163361, 163952, 164545, 
	};

	/**
	 * Pitch Envelope Slider Table
	 */
	public static final int [] LinearSlideDownTable = new int [] 
	{
		65536, 65299, 65064, 64830, 64596, 64363, 64131, 63900, 
		63670, 63440, 63212, 62984, 62757, 62531, 62305, 62081, 
		61857, 61634, 61412, 61191, 60970, 60751, 60532, 60314, 
		60096, 59880, 59664, 59449, 59235, 59021, 58809, 58597, 
		58385, 58175, 57965, 57757, 57548, 57341, 57134, 56928, 
		56723, 56519, 56315, 56112, 55910, 55709, 55508, 55308, 
		55108, 54910, 54712, 54515, 54318, 54123, 53928, 53733, 
		53540, 53347, 53154, 52963, 52772, 52582, 52392, 52204, 
		52015, 51828, 51641, 51455, 51270, 51085, 50901, 50717, 
		50535, 50353, 50171, 49990, 49810, 49631, 49452, 49274, 
		49096, 48919, 48743, 48567, 48392, 48218, 48044, 47871, 
		47698, 47526, 47355, 47185, 47014, 46845, 46676, 46508, 
		46340, 46173, 46007, 45841, 45676, 45511, 45347, 45184, 
		45021, 44859, 44697, 44536, 44376, 44216, 44056, 43898, 
		43740, 43582, 43425, 43268, 43112, 42957, 42802, 42648, 
		42494, 42341, 42189, 42037, 41885, 41734, 41584, 41434, 
		41285, 41136, 40988, 40840, 40693, 40546, 40400, 40254, 
		40109, 39965, 39821, 39677, 39534, 39392, 39250, 39108, 
		38967, 38827, 38687, 38548, 38409, 38270, 38132, 37995, 
		37858, 37722, 37586, 37450, 37315, 37181, 37047, 36913, 
		36780, 36648, 36516, 36384, 36253, 36122, 35992, 35862, 
		35733, 35604, 35476, 35348, 35221, 35094, 34968, 34842, 
		34716, 34591, 34466, 34342, 34218, 34095, 33972, 33850, 
		33728, 33606, 33485, 33364, 33244, 33124, 33005, 32886, 
		32768, 32649, 32532, 32415, 32298, 32181, 32065, 31950, 
		31835, 31720, 31606, 31492, 31378, 31265, 31152, 31040, 
		30928, 30817, 30706, 30595, 30485, 30375, 30266, 30157, 
		30048, 29940, 29832, 29724, 29617, 29510, 29404, 29298, 
		29192, 29087, 28982, 28878, 28774, 28670, 28567, 28464, 
		28361, 28259, 28157, 28056, 27955, 27854, 27754, 27654, 
		27554, 27455, 27356, 27257, 27159, 27061, 26964, 26866, 
		26770, 26673, 26577, 26481, 26386, 26291, 26196, 26102, 
	};
	/**
	 * These are the values of the first octave of the above protracker_fineTunedPeriods
	 * This table is used by the XM_AMIGA_TABLE routine
	 */
	protected static final int LOGFAC = 128; // i.e. 3 Ocatves plus 4 bit qualityshift --> 1<<7=128
	public static final int [] logtab = new int[]
    {
		LOGFAC*907, LOGFAC*900, LOGFAC*894, LOGFAC*887, LOGFAC*881, LOGFAC*875, LOGFAC*868, LOGFAC*862,
		LOGFAC*856, LOGFAC*850, LOGFAC*844, LOGFAC*838, LOGFAC*832, LOGFAC*826, LOGFAC*820, LOGFAC*814,
		LOGFAC*808, LOGFAC*802, LOGFAC*796, LOGFAC*791, LOGFAC*785, LOGFAC*779, LOGFAC*774, LOGFAC*768,
		LOGFAC*762, LOGFAC*757, LOGFAC*752, LOGFAC*746, LOGFAC*741, LOGFAC*736, LOGFAC*730, LOGFAC*725,
		LOGFAC*720, LOGFAC*715, LOGFAC*709, LOGFAC*704, LOGFAC*699, LOGFAC*694, LOGFAC*689, LOGFAC*684,
		LOGFAC*678, LOGFAC*675, LOGFAC*670, LOGFAC*665, LOGFAC*660, LOGFAC*655, LOGFAC*651, LOGFAC*646,
		LOGFAC*640, LOGFAC*636, LOGFAC*632, LOGFAC*628, LOGFAC*623, LOGFAC*619, LOGFAC*614, LOGFAC*610,
		LOGFAC*604, LOGFAC*601, LOGFAC*597, LOGFAC*592, LOGFAC*588, LOGFAC*584, LOGFAC*580, LOGFAC*575,
		LOGFAC*570, LOGFAC*567, LOGFAC*563, LOGFAC*559, LOGFAC*555, LOGFAC*551, LOGFAC*547, LOGFAC*543,
		LOGFAC*538, LOGFAC*535, LOGFAC*532, LOGFAC*528, LOGFAC*524, LOGFAC*520, LOGFAC*516, LOGFAC*513,
		LOGFAC*508, LOGFAC*505, LOGFAC*502, LOGFAC*498, LOGFAC*494, LOGFAC*491, LOGFAC*487, LOGFAC*484,
		LOGFAC*480, LOGFAC*477, LOGFAC*474, LOGFAC*470, LOGFAC*467, LOGFAC*463, LOGFAC*460, LOGFAC*457,
		LOGFAC*453, LOGFAC*450, LOGFAC*447, LOGFAC*443, LOGFAC*440, LOGFAC*437, LOGFAC*434, LOGFAC*431,
		LOGFAC*428 //!Need this for interpolating!
	};

	/**
	 * Triton's linear periods to frequency translation table (for XM modules)
	 */
	public static final int [] lintab = new int[]
	{
		535232, 534749, 534266, 533784, 533303, 532822, 532341, 531861, 531381, 530902, 530423, 529944, 529466, 528988, 528511, 528034,
		527558, 527082, 526607, 526131, 525657, 525183, 524709, 524236, 523763, 523290, 522818, 522346, 521875, 521404, 520934, 520464,
		519994, 519525, 519057, 518588, 518121, 517653, 517186, 516720, 516253, 515788, 515322, 514858, 514393, 513929, 513465, 513002,
		512539, 512077, 511615, 511154, 510692, 510232, 509771, 509312, 508852, 508393, 507934, 507476, 507018, 506561, 506104, 505647,
		505191, 504735, 504280, 503825, 503371, 502917, 502463, 502010, 501557, 501104, 500652, 500201, 499749, 499298, 498848, 498398,
		497948, 497499, 497050, 496602, 496154, 495706, 495259, 494812, 494366, 493920, 493474, 493029, 492585, 492140, 491696, 491253,
		490809, 490367, 489924, 489482, 489041, 488600, 488159, 487718, 487278, 486839, 486400, 485961, 485522, 485084, 484647, 484210,
		483773, 483336, 482900, 482465, 482029, 481595, 481160, 480726, 480292, 479859, 479426, 478994, 478562, 478130, 477699, 477268,
		476837, 476407, 475977, 475548, 475119, 474690, 474262, 473834, 473407, 472979, 472553, 472126, 471701, 471275, 470850, 470425,
		470001, 469577, 469153, 468730, 468307, 467884, 467462, 467041, 466619, 466198, 465778, 465358, 464938, 464518, 464099, 463681,
		463262, 462844, 462427, 462010, 461593, 461177, 460760, 460345, 459930, 459515, 459100, 458686, 458272, 457859, 457446, 457033,
		456621, 456209, 455797, 455386, 454975, 454565, 454155, 453745, 453336, 452927, 452518, 452110, 451702, 451294, 450887, 450481,
		450074, 449668, 449262, 448857, 448452, 448048, 447644, 447240, 446836, 446433, 446030, 445628, 445226, 444824, 444423, 444022,
		443622, 443221, 442821, 442422, 442023, 441624, 441226, 440828, 440430, 440033, 439636, 439239, 438843, 438447, 438051, 437656,
		437261, 436867, 436473, 436079, 435686, 435293, 434900, 434508, 434116, 433724, 433333, 432942, 432551, 432161, 431771, 431382,
		430992, 430604, 430215, 429827, 429439, 429052, 428665, 428278, 427892, 427506, 427120, 426735, 426350, 425965, 425581, 425197,
		424813, 424430, 424047, 423665, 423283, 422901, 422519, 422138, 421757, 421377, 420997, 420617, 420237, 419858, 419479, 419101,
		418723, 418345, 417968, 417591, 417214, 416838, 416462, 416086, 415711, 415336, 414961, 414586, 414212, 413839, 413465, 413092,
		412720, 412347, 411975, 411604, 411232, 410862, 410491, 410121, 409751, 409381, 409012, 408643, 408274, 407906, 407538, 407170,
		406803, 406436, 406069, 405703, 405337, 404971, 404606, 404241, 403876, 403512, 403148, 402784, 402421, 402058, 401695, 401333,
		400970, 400609, 400247, 399886, 399525, 399165, 398805, 398445, 398086, 397727, 397368, 397009, 396651, 396293, 395936, 395579,
		395222, 394865, 394509, 394153, 393798, 393442, 393087, 392733, 392378, 392024, 391671, 391317, 390964, 390612, 390259, 389907,
		389556, 389204, 388853, 388502, 388152, 387802, 387452, 387102, 386753, 386404, 386056, 385707, 385359, 385012, 384664, 384317,
		383971, 383624, 383278, 382932, 382587, 382242, 381897, 381552, 381208, 380864, 380521, 380177, 379834, 379492, 379149, 378807,
		378466, 378124, 377783, 377442, 377102, 376762, 376422, 376082, 375743, 375404, 375065, 374727, 374389, 374051, 373714, 373377,
		373040, 372703, 372367, 372031, 371695, 371360, 371025, 370690, 370356, 370022, 369688, 369355, 369021, 368688, 368356, 368023,
		367691, 367360, 367028, 366697, 366366, 366036, 365706, 365376, 365046, 364717, 364388, 364059, 363731, 363403, 363075, 362747,
		362420, 362093, 361766, 361440, 361114, 360788, 360463, 360137, 359813, 359488, 359164, 358840, 358516, 358193, 357869, 357547,
		357224, 356902, 356580, 356258, 355937, 355616, 355295, 354974, 354654, 354334, 354014, 353695, 353376, 353057, 352739, 352420,
		352103, 351785, 351468, 351150, 350834, 350517, 350201, 349885, 349569, 349254, 348939, 348624, 348310, 347995, 347682, 347368,
		347055, 346741, 346429, 346116, 345804, 345492, 345180, 344869, 344558, 344247, 343936, 343626, 343316, 343006, 342697, 342388,
		342079, 341770, 341462, 341154, 340846, 340539, 340231, 339924, 339618, 339311, 339005, 338700, 338394, 338089, 337784, 337479,
		337175, 336870, 336566, 336263, 335959, 335656, 335354, 335051, 334749, 334447, 334145, 333844, 333542, 333242, 332941, 332641,
		332341, 332041, 331741, 331442, 331143, 330844, 330546, 330247, 329950, 329652, 329355, 329057, 328761, 328464, 328168, 327872,
		327576, 327280, 326985, 326690, 326395, 326101, 325807, 325513, 325219, 324926, 324633, 324340, 324047, 323755, 323463, 323171,
		322879, 322588, 322297, 322006, 321716, 321426, 321136, 320846, 320557, 320267, 319978, 319690, 319401, 319113, 318825, 318538,
		318250, 317963, 317676, 317390, 317103, 316817, 316532, 316246, 315961, 315676, 315391, 315106, 314822, 314538, 314254, 313971,
		313688, 313405, 313122, 312839, 312557, 312275, 311994, 311712, 311431, 311150, 310869, 310589, 310309, 310029, 309749, 309470,
		309190, 308911, 308633, 308354, 308076, 307798, 307521, 307243, 306966, 306689, 306412, 306136, 305860, 305584, 305308, 305033,
		304758, 304483, 304208, 303934, 303659, 303385, 303112, 302838, 302565, 302292, 302019, 301747, 301475, 301203, 300931, 300660,
		300388, 300117, 299847, 299576, 299306, 299036, 298766, 298497, 298227, 297958, 297689, 297421, 297153, 296884, 296617, 296349,
		296082, 295815, 295548, 295281, 295015, 294749, 294483, 294217, 293952, 293686, 293421, 293157, 292892, 292628, 292364, 292100,
		291837, 291574, 291311, 291048, 290785, 290523, 290261, 289999, 289737, 289476, 289215, 288954, 288693, 288433, 288173, 287913,
		287653, 287393, 287134, 286875, 286616, 286358, 286099, 285841, 285583, 285326, 285068, 284811, 284554, 284298, 284041, 283785,
		283529, 283273, 283017, 282762, 282507, 282252, 281998, 281743, 281489, 281235, 280981, 280728, 280475, 280222, 279969, 279716,
		279464, 279212, 278960, 278708, 278457, 278206, 277955, 277704, 277453, 277203, 276953, 276703, 276453, 276204, 275955, 275706,
		275457, 275209, 274960, 274712, 274465, 274217, 273970, 273722, 273476, 273229, 272982, 272736, 272490, 272244, 271999, 271753,
		271508, 271263, 271018, 270774, 270530, 270286, 270042, 269798, 269555, 269312, 269069, 268826, 268583, 268341, 268099, 267857
	};

	/**
	 * Sinus table
	 */
	public static final int [] ModSinusTable = new int[]
	{
		   0,   24,   49,   74,   97,  120,  141,  161,  180,  197,  212,  224,  235,  244,  250,  253,
		 255,  253,  250,  244,  235,  224,  212,  197,  180,  161,  141,  120,   97,   74,   49,   24,
		   0,  -24,  -49,  -74,  -97, -120, -141, -161, -180, -197, -212, -224, -235, -244, -250, -253,
		-255, -253, -250, -244, -235, -224, -212, -197, -180, -161, -141, -120,  -97,  -74,  -49,  -24
	};

	/**
	 * Triangle wave table (ramp down)
	 */
	public static final int [] ModRampDownTable = new int[]
	{
		   0,   -8,  -16,  -24,  -32,  -40,  -48,  -56,  -64,  -72,  -80,  -88,  -96, -104, -112, -120,
		-128, -136, -144, -152, -160, -168, -176, -184, -192, -200, -208, -216, -224, -232, -240, -248,
		 255,  247,  239,  231,  223,  215,  207,  199,  191,  183,  175,  167,  159,  151,  143,  135,
		 127,  119,  113,  103,   95,   87,   79,   71,   63,   55,   47,   39,   31,   23,   15,    7,
	};

	/**
	 * Square wave table (normaly useless, but this keeps the used logic the same)
	 */
	public static final int [] ModSquareTable = new int []
	{
		 255,  255,  255,  255,  255,  255,  255,  255,  255,  255,  255,  255,  255,  255,  255,  255,
		 255,  255,  255,  255,  255,  255,  255,  255,  255,  255,  255,  255,  255,  255,  255,  255,
		-255, -255, -255, -255, -255, -255, -255, -255, -255, -255, -255, -255, -255, -255, -255, -255,
		-255, -255, -255, -255, -255, -255, -255, -255, -255, -255, -255, -255, -255, -255, -255, -255
	};

	/**
	 * Random wave table
	 */
	public static final int [] ModRandomTable = new int []
	{
		 196, -254,  -86,  176,  204,   82, -130, -188,  250,   40, -142, -172, -140,  -64,  -32, -192,
		  34,  144,  214,  -10,  232, -138, -124,  -80,   20, -122,  130,  218,  -36,  -76,  -26, -152,
		 -46,  176,   42, -188,   16,  212,   42, -224,   12,  218,   40, -176,  -60,   18, -254,  236,
		  84,  -68,  178,   -8, -102, -144,   42,  -58,  224,  246,  168, -202, -184,  196, -108, -190
	};
	/**
	 * The FT2 vibrato table
	 */
	public static final int [] ft2VibratoTable = new int[]
	{
		  0,  -2,  -3,  -5,  -6,  -8,  -9, -11, -12, -14, -16, -17, -19, -20, -22, -23,
		-24, -26, -27, -29, -30, -32, -33, -34, -36, -37, -38, -39, -41, -42, -43, -44,
		-45, -46, -47, -48, -49, -50, -51, -52, -53, -54, -55, -56, -56, -57, -58, -59,
		-59, -60, -60, -61, -61, -62, -62, -62, -63, -63, -63, -64, -64, -64, -64, -64,
		-64, -64, -64, -64, -64, -64, -63, -63, -63, -62, -62, -62, -61, -61, -60, -60,
		-59, -59, -58, -57, -56, -56, -55, -54, -53, -52, -51, -50, -49, -48, -47, -46,
		-45, -44, -43, -42, -41, -39, -38, -37, -36, -34, -33, -32, -30, -29, -27, -26,
		-24, -23, -22, -20, -19, -17, -16, -14, -12, -11,  -9,  -8,  -6,  -5,  -3,  -2,
		  0,   2,   3,   5,   6,   8,   9,  11,  12,  14,  16,  17,  19,  20,  22,  23,
		 24,  26,  27,  29,  30,  32,  33,  34,  36,  37,  38,  39,  41,  42,  43,  44,
		 45,  46,  47,  48,  49,  50,  51,  52,  53,  54,  55,  56,  56,  57,  58,  59,
		 59,  60,  60,  61,  61,  62,  62,  62,  63,  63,  63,  64,  64,  64,  64,  64,
		 64,  64,  64,  64,  64,  64,  63,  63,  63,  62,  62,  62,  61,  61,  60,  60,
		 59,  59,  58,  57,  56,  56,  55,  54,  53,  52,  51,  50,  49,  48,  47,  46,
		 45,  44,  43,  42,  41,  39,  38,  37,  36,  34,  33,  32,  30,  29,  27,  26,
		 24,  23,  22,  20,  19,  17,  16,  14,  12,  11,   9,   8,   6,   5,   3,   2
	};
	/**
	 * Some constants
	 */
	public static final int BASEFREQUENCY = 8363;
	public static final int BASEPERIOD = 428;
	/**
	 * VU-Meter constants
	 */
	public static final String SCROLLY_BLANKS = "     ";
	
	/**
	 * The frequency tables supported!
	 * AMIGA_TABLE, XM_AMIGA_TABLE and XM_LINEAR_TABLE have
	 * to have the highest Numbers, just to
	 * send a noteindex if (ft>=AMIGA_TABLE)
	 */
	public static final int STM_S3M_TABLE 	= 0;
	public static final int IT_LINEAR_TABLE = 1;
	public static final int AMIGA_TABLE 	= 2;
	public static final int XM_AMIGA_TABLE 	= 4;
	public static final int XM_LINEAR_TABLE = 8;
	public static final int IT_AMIGA_TABLE = 16;

	public static final int VOLUMESHIFT = 8;
	public static final int VOLUMESHIFT_FULL = 8;
	public static final int MAXVOLUME = 64 << VOLUMESHIFT;
	public static final int MAXVOLUMESHIFT = 32; // This is the SHIFT for reducing VOLUMESHIFT (16Bit) AND GlobalVolume

	public static final int SHIFT = 16;
	public static final int SHIFT_ONE = 1<<SHIFT;
	public static final int SHIFT_MASK = SHIFT_ONE-1;

	public static final int PERIOD_SHIFT = 4;
	public static final int SAMPLE_SHIFT = 12;

	public static final int VOL_RAMP_FRAC = 4;
	public static final int VOL_RAMP_LEN  = 1<<VOL_RAMP_FRAC;
	public static final int VOLRAMPLEN_MS = 146;

	public static final int XBASS_DELAY = 14;
	public static final int XBASS_BUFFER = 64;

	public static final int MODTYPE_MOD = 1;
	public static final int MODTYPE_XM = 2;
	public static final int MODTYPE_S3M = 4;
	public static final int MODTYPE_IT = 8;

	public static final int SM_PCMS		= 	0x00;					// PCM 8 Bit Signed
	public static final int SM_PCMU		= 	0x01;					// PCM 8 Bit unsigned
	public static final int SM_PCMD 	=	0x02;					// PCM 8 Bit delta values
	public static final int SM_ADPCM4 	=	0x03;					// ADPCM-packed (4 Bit)
	public static final int SM_16BIT 	=	0x04;					// 16 BIT
	public static final int SM_STEREO	= 	0x08;					// STEREO
	public static final int SM_PCM16S	= 	SM_PCMS | SM_16BIT;		// PCM 16 Bit Signed
	public static final int SM_PCM16U	= 	SM_PCMU | SM_16BIT;		// PCM 16 Bit unsigned
	public static final int SM_PCM16D 	=	SM_PCMD | SM_16BIT;		// PCM 16 Bit delta values
	public static final int SM_PCM16M 	=	SM_ADPCM4 | SM_16BIT;	// PCM 16 Bit motorola order
	public static final int SM_STPCM8S	= 	SM_PCMS | SM_STEREO;	// PCM 8 Bit Signed STEREO
	public static final int SM_STPCM8U	= 	SM_PCMU | SM_STEREO;	// PCM 8 Bit unsigned STEREO
	public static final int SM_STPCM8D 	=	SM_PCMD | SM_STEREO;	// PCM 8 Bit delta values STEREO
	public static final int SM_STPCM16S	= 	SM_PCM16S | SM_STEREO;	// PCM 16 Bit Signed STEREO
	public static final int SM_STPCM16U	= 	SM_PCM16U | SM_STEREO;	// PCM 16 Bit unsigned STEREO
	public static final int SM_STPCM16D	=	SM_PCM16D | SM_STEREO;	// PCM 16 Bit delta values STEREO
	public static final int SM_STPCM16M	=	SM_PCM16M | SM_STEREO;	// PCM 16 Bit motorola order STEREO
	// IT Packed (>2.14)
	public static final int SM_IT2148	=	0x10;
	public static final int SM_IT21416	=	SM_IT2148 | SM_16BIT;
	public static final int SM_IT2158	=	0x12;
	public static final int SM_IT21516	=	SM_IT2158 | SM_16BIT;
	
	// Loop Types
	public static final int LOOP_ON						=	0x01;
	public static final int LOOP_SUSTAIN_ON				=	0x02;
	public static final int LOOP_IS_PINGPONG			=	0x04;
	public static final int LOOP_SUSTAIN_IS_PINGPONG	=	0x08;
	
	// NNA Types
	public static final int NNA_CUT 		= 0;
	public static final int NNA_CONTINUE 	= 1;
	public static final int NNA_OFF 		= 2;
	public static final int NNA_FADE 		= 3;
	
	// KeyOff and NoteCut values
	public static final int KEY_OFF			= -1;
	public static final int NOTE_CUT		= -2;
	
	// Filter Modes
	public static final int FLTMODE_LOWPASS		= 0;
	public static final int FLTMODE_HIGHPASS	= 1;
	public static final int FLTMODE_BANDPASS	= 2;
	public static final int FILTER_SHIFT_BITS	= 32;
	public static final double FILTER_PRECISION	= (double)(1L << FILTER_SHIFT_BITS);

	// Module flags
	public static final int SONG_EMBEDMIDICFG	= 0x0001;
	public static final int SONG_FASTVOLSLIDES	= 0x0002;
	public static final int SONG_ITOLDEFFECTS	= 0x0004;
	public static final int SONG_ITCOMPATMODE	= 0x0008;
	public static final int SONG_LINEARSLIDES	= 0x0010;
	public static final int SONG_PATTERNLOOP	= 0x0020;
	public static final int SONG_STEP			= 0x0040;
	public static final int SONG_PAUSED			= 0x0080;
	public static final int SONG_FADINGSONG		= 0x0100;
	public static final int SONG_ENDREACHED		= 0x0200;
	public static final int SONG_GLOBALFADE		= 0x0400;
	public static final int SONG_CPUVERYHIGH	= 0x0800;
	public static final int SONG_FIRSTTICK		= 0x1000;
	public static final int SONG_MPTFILTERMODE	= 0x2000;
	public static final int SONG_SURROUNDPAN	= 0x4000;
	public static final int SONG_EXFILTERRANGE	= 0x8000;
	public static final int SONG_AMIGALIMITS	= 0x10000;
	
	// Player flags
	public static final int PLAYER_LOOP_DEACTIVATED = 0;
	public static final int PLAYER_LOOP_FADEOUT = 1;
	public static final int PLAYER_LOOP_IGNORE = 2;
	

	/* SERVICE METHODS -------------------------------------------------------*/
	/**
	 * For the patterndisplay: retrieve a String for the given note index
	 * @param index
	 * @return
	 */
	
	/**
	 * For the patterndisplay: retrieve a String for the given period
	 * @param period
	 * @return
	 */
	
	/**
	 * get the index for the note
	 * @param period
	 * @return
	 */

	/**
	 * Get the period of the nearest halftone
	 * @param period
	 * @return
	 */
	
	/**
	 * Loads a C-Type String. In C/C++ strings end with a NULL-byte. We search for it
	 * and skip the rest. Furthermore we convert to UNICODE
	 * By the way: sometimes the C-Strings are not terminated. We need to do that
	 * ourself than!
	 * @param input
	 * @param start
	 * @param length
	 * @return
	 */
	public static String retrieveAsString(final byte [] input, final int start, final int length)
	{
		return retrieveAsString(input, start, length, currentCoding);
	}
	/**
	 * Loads a C-Type String. In C/C++ strings end with a NULL-byte. We search for it
	 * and skip the rest. Furthermore we convert to UNICODE
	 * By the way: sometimes the C-Strings are not terminated. We need to do that
	 * ourself than!
	 * @param input
	 * @param start
	 * @param length
	 * @param coding
	 * @return
	 */
	public static String retrieveAsString(final byte [] input, final int start, final int length, final String coding)
	{
		String str = null;
		try
		{
			str = new String(input, start, length, coding);
		}
		catch (UnsupportedEncodingException ex)
		{
			str = new String(input, start, length);
		}
		// find a null byte and delete it
		StringBuilder b = new StringBuilder(str);
		int index = 0;
		while (index<b.length() && b.charAt(index)!=0) index++;
		b.delete(index, b.length());
		return b.toString();
	}
	/**
	 * Converts a string to an array of bytes with the length specified
	 * @since 23.12.2013
	 * @param str the string to convert
	 * @param length the size of the byte array
	 * @return the array of bytes converted from the string
	 */
	public static byte[] getBytesFromString(final String str, final int length, final String coding)
	{
		byte [] result = new byte [length];
		int len = str.length();
		if (len>length) len = length;
		try
		{
			System.arraycopy(str.getBytes(coding), 0, result, 0, len);
		}
		catch (UnsupportedEncodingException ex)
		{
			System.arraycopy(str.getBytes(), 0, result, 0, len);
		}
		return result;
	}
	/**
	 * Displays a value as a hex-value, using #digits
	 * @param value
	 * @param digits
	 * @return
	 */
	public static String getAsHex(final int value, final int digits)
	{
		StringBuilder result = new StringBuilder();
		String hex = Integer.toString(value, 16).toUpperCase();
		for (int i=0; i<digits-hex.length(); i++) result.append('0');
		return (result.append(hex)).toString();
	}

	// Konversions for read bytes! *********************************************
	/**
	 * Converts an Intel like stored word to an integer
	 * @param buf
	 * @param offset
	 * @return
	 */
	public static int convertIntelWordToInt(final byte[] buf, final int offset)
	{
		return (buf[offset]&0xFF) | ((buf[offset+1]&0xFF)<<8);
	}
	/**
	 * Converts an Intel like stored 3 Byte  to an integer
	 * @param buf
	 * @param offset
	 * @return
	 */
	public static int convertIntel3ByteToInt(final byte[] buf, final int offset)
	{
		return (buf[offset]&0xFF) | ((buf[offset+1]&0xFF)<<8) | ((buf[offset+2]&0xFF)<<16);
	}
	/**
	 * Converts an Intel like stored dword to an integer (less significant byte first)
	 * @param buf
	 * @param offset
	 * @return
	 */
	public static int convertIntelDWordToInt(final byte[] buf, final int offset)
	{
		return (buf[offset]&0xFF) | ((buf[offset+1]&0xFF)<<8) | ((buf[offset+2]&0xFF)<<16) | ((buf[offset+3]&0xFF)<<24);
	}
	/**
	 * Converts an Motorola 86000er word to an integer
	 * @param buf
	 * @param offset
	 * @return
	 */
	public static int convertWordToInt(final byte[] buf, final int offset)
	{
		return ((buf[offset]&0xFF)<<8) | (buf[offset+1]&0xFF);
	}
	/**
	 * Converts an Motorola 86000er 3 Byte to an integer
	 * @param buf
	 * @param offset
	 * @return
	 */
	public static int convert3ByteToInt(final byte[] buf, final int offset)
	{
		return ((buf[offset]&0xFF)<<16) | ((buf[offset+1]&0xFF)<<8) | (buf[offset+2]&0xFF);
	}
	/**
	 * Converts an Motorola 86000er dword to an integer (most significant byte first)
	 * @param buf
	 * @param offset
	 * @return
	 */
	public static int convertDWordToInt(final byte[] buf, final int offset)
	{
		return ((buf[offset]&0xFF)<<24) | ((buf[offset+1]&0xFF)<<16) | ((buf[offset+2]&0xFF)<<8) | (buf[offset+3]&0xFF);
	}
	
	/**
	 * Converts an integer to an Motorola 86000er dword byte array
	 * @since 23.12.2013
	 * @param value
	 * @param offset
	 * @return
	 */
	public static byte [] convertIntToDWord(final int value)
	{
		byte[] buf = new byte[4];
		buf[0] = (byte)((value>>24)&0xFF);
		buf[1] = (byte)((value>>16)&0xFF);
		buf[2] = (byte)((value>> 8)&0xFF);
		buf[3] = (byte)((value    )&0xFF);
		return buf;
	}

	// Konversions for Sampledata! *********************************************
	/**
	 * converts signed 8 bit values to signed 16 bit
	 * @param sample
	 * @return
	 */
	public static int promoteSigned8BitToSigned16Bit(final byte sample)
	{
		return (sample)<<8;
	}
	/**
	 * converts signed 8 bit values to signed 24 bit
	 * @param sample
	 * @return
	 */
	public static int promoteSigned8BitToSigned24Bit(final byte sample)
	{
		return (sample)<<16;
	}
	/**
	 * converts signed 16 bit values to signed 24 bit
	 * @param sample
	 * @return
	 */
	public static int promoteSigned16BitToSigned24Bit(final int sample)
	{
		return sample<<8;
	}
	/**
	 * converts unsigned 8 bit values to signed 16 bit
	 * @param sample
	 * @return
	 */
	public static int promoteUnsigned8BitToSigned16Bit(final byte sample)
	{
		return (((sample)&0xFF)-0x80)<<8;
	}
	/**
	 * converts unsigned 16 bit values to signed 16 bit
	 * @param sample
	 * @return
	 */
	public static int promoteUnsigned16BitToSigned16Bit(final int sample)
	{
		return (sample&0xFFFF)-0x8000;
	}
	/**
	 * converts unsigned 8 bit values to signed 24 bit
	 * @since 26.05.2006
	 * @param sample
	 * @return
	 */
	public static int promoteUnsigned8BitToSigned24Bit(final byte sample)
	{
		return (((sample)&0xFF)-0x80)<<16;
	}
	/**
	 * converts unsigned 16 bit values to signed 24 bit
	 * @param sample
	 * @return
	 */
	public static int promoteUnsigned16BitToSigned24Bit(final int sample)
	{
		return ((sample&0xFFFF)-0x8000)<<8;
	}
	/**
	 * converts unsigned 16 bit values to signed 24 bit
	 * @param sample
	 * @return
	 */
	public static int promoteUnsigned24BitToSigned24Bit(final int sample)
	{
		return (sample&0xFFFFFF)-0x800000;
	}
	/**
	 * converts signed 16 bit values to unsigned 8 bit
	 * @param sample
	 * @return
	 */
	public static int promoteSigned16BitToUnsigned8Bit(final int sample)
	{
		return ((sample>>8)+0x80)&0xFF;
	}
	/**
	 * converts signed 24 bit values to unsigned 24 bit
	 * @param sample
	 * @return
	 */
	public static int promoteSigned24BitToUnsigned24Bit(final int sample)
	{
		return (sample+0x800000)&0xFFFFFF;
	}

//	/**
//	 * This routine will convert any inputbuffer described by the parameters
//	 * into signed 24 Bit samples
//	 * The result buffer will contain as many channels as the input buffer
//	 * @since 17.10.2007
//	 * @param result
//	 * @param pos
//	 * @param buffer
//	 * @param ox
//	 * @param anz
//	 * @param channels
//	 * @param sampleSizeInBits
//	 * @param isBigEndian
//	 * @param isSigned
//	 */
//	public static void convertAnyToSigned24Bit(final int [] result, int pos, final byte [] buffer, int ox, final int anz, final int channels, final int sampleSizeInBits, final boolean isBigEndian, final boolean isSigned)
//	{
//		final int bytesPerChannel = sampleSizeInBits>>3;
//		final int anzSamples = anz/(bytesPerChannel*channels);
//		int sample;
//		for (int i=0; i<anzSamples; i++)
//		{
//			for (int c=0; c<channels; c++)
//			{
//				sample=0;
//				if (isBigEndian)
//				{
//					for (int b=bytesPerChannel-1, s=24-sampleSizeInBits; b>=0; b--, s+=8)
//						sample |= ((buffer[ox+b])&0xFF)<<s;
//				}
//				else
//				{
//					for (int b=0, s=24-sampleSizeInBits; b<bytesPerChannel; b++, s+=8)
//						sample |= ((buffer[ox+b])&0xFF)<<s;
//				}
//				if (isSigned)
//				{
//					if ((sample & 0x800000)!=0) sample|=0xFF000000;
//				}
//				else
//				{
//					sample = (sample & 0xFFFFFF)-0x800000;
//				}
//				result[pos++] = sample;
//				ox += bytesPerChannel;
//			}
//		}
//	}
	/**
	 * @since 22.06.2013
	 * @param gridx
	 * @param gridy
	 * @param gridheight
	 * @param gridwidth
	 * @param fill
	 * @param anchor
	 * @param weightx
	 * @param weighty
	 * @return
	 */
	public static java.awt.GridBagConstraints getGridBagConstraint(int gridx, int gridy, int gridheight, int gridwidth, int fill, int anchor, double weightx, double weighty)
	{
		return getGridBagConstraint(gridx, gridy, gridheight, gridwidth, fill, anchor, weightx, weighty, new java.awt.Insets(4, 4, 4, 4));
	}
	public static java.awt.GridBagConstraints getGridBagConstraint(int gridx, int gridy, int gridheight, int gridwidth, int fill, int anchor, double weightx, double weighty, Insets insets)
	{
		java.awt.GridBagConstraints constraints = new java.awt.GridBagConstraints();
		constraints.gridx = gridx; 
		constraints.gridy = gridy;
		constraints.gridheight = gridheight;
		constraints.gridwidth = gridwidth;
		constraints.fill = fill;
		constraints.anchor = anchor;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.insets = insets;
		return constraints;
	}
	/**
	 * Get the location for centered Dialog
	 * @since 22.06.2013
	 * @param centerThis
	 * @return
	 */
    public static java.awt.Point getFrameCenteredLocation(final java.awt.Component centerThis, final Component parent)
	{
    	java.awt.Dimension screenSize = (parent==null)?java.awt.Toolkit.getDefaultToolkit().getScreenSize():parent.getSize();

		int x = (screenSize.width - centerThis.getWidth()) >> 1;
		int y = (screenSize.height - centerThis.getHeight()) >> 1;

		if (parent!=null)
		{
			x+=parent.getX();
			y+=parent.getY();
		}
		if (x<0) x=0;
		if (y<0) y=0;

		return new java.awt.Point(x, y);
	}
	/**
	 * Register the droplistener to all components... 
	 * @since: 12.10.2007
	 * @param list ArrayList of resulting DropTarget-Classes
	 * @param basePanel
	 * @param myListener
	 */
	public static void registerDropListener(ArrayList<DropTarget> list, Container basePanel, PlaylistDropListener myListener)
	{
		list.add(new DropTarget(basePanel, DnDConstants.ACTION_COPY_OR_MOVE, myListener));
	    
    	Component[] components = basePanel.getComponents();
	    for (int i=0; i<components.length; i++)
	    {
		    Component component = components[i];
		    if (component instanceof Container)
		    	registerDropListener(list, (Container)component, myListener);
		    else
		    	list.add(new DropTarget(component, DnDConstants.ACTION_COPY_OR_MOVE, myListener));
	    }
	}
	/**
	 * Compares to URLs and returns true, if they are equal and OK. This is done via
	 * the URI - as URLs do a domain name lookup which will block if no
	 * DNS Lookup is possible 
	 * @since 01.04.2013
	 * @param url1
	 * @param url2
	 * @return
	 * @throws URISyntaxException
	 */
	public static boolean isEqualURL(final URL url1, final URL url2)
	{
		if (url1!=null && url2!=null)
		{
			try
			{
				URI uri1 = url1.toURI();
				URI uri2 = url2.toURI();
				return uri1.equals(uri2);
			}
			catch (URISyntaxException ex)
			{
				/* NOOP */
			}
		}
		return false;
	}
        
	/**
	 * @param file
	 * @return
	 * @since 14.02.2013
	 */
	public static URL createURLfromFile(File file)
	{
		if (!file.exists())
		{
			try
			{
				String path = file.getPath();
				StringBuilder b = new StringBuilder((File.separatorChar != '/') ? path.replace(File.separatorChar, '/') : path);
				if (file.isDirectory() && b.charAt(b.length() - 1) != '/') b.append('/');
				if (b.length()>2 && b.charAt(0)=='/' && b.charAt(1)=='/') b.insert(0, "//");
				URI uri = new URI("file", null, b.toString(), null);
				return uri.toURL();
			}
			catch (URISyntaxException e)
			{
				// cannot happen...
			}
			catch (MalformedURLException ex)
			{
				// should not happen ;)
			}
		}
		try
		{
			return file.toURI().toURL();
		}
		catch (MalformedURLException ex)
		{
		}
		return null;
	}
	/**
	 * @since 01.05.2013
	 * @param urlLine
	 * @return a URL in correct form
	 */
	public static URL createURLfromString(String urlLine)
	{
		try
		{
			URL url = new URL(urlLine);
			try
			{
				URI uri = new URI(url.getProtocol(), null, url.getHost(), url.getPort(), url.getPath(), null, null);
				return uri.toURL();
			}
			catch (URISyntaxException e)
			{
				return url;
			}
		}
		catch (MalformedURLException ex)
		{
			return createURLfromFile(new File(urlLine));
		}
	}
	public static String createStringFomURL(URL url)
	{
		return createStringFromURLString(url.toExternalForm());
	}
	public static String createStringFromURLString(String url)
	{
		try
		{
			return URLDecoder.decode(url, "UTF-8");
		}
		catch (UnsupportedEncodingException ex)
		{
		}
		return url;
	}
	public static String getFileNameFrom(String fileName)
	{
		return fileName.substring(fileName.lastIndexOf('/') + 1);
	}
	public static String getFileNameFromURL(URL url)
	{
		return getFileNameFrom(url.getPath());
	}
	public static String getExtensionFrom(String fileName)
	{
		return fileName.substring(fileName.lastIndexOf('.')+1).toLowerCase();
	}
	public static String getExtensionFromURL(URL url)
	{
		return getExtensionFrom(url.getPath());
	}
	public static String getPreceedingExtensionFrom(String fileName)
	{
		fileName = fileName.substring(fileName.lastIndexOf('\\')+1);
		fileName = fileName.substring(fileName.lastIndexOf('/')+1);
		int dot = fileName.indexOf('.');
		if (dot>0)
			return fileName.substring(0, dot).toLowerCase();
		else
			return "";
	}
	public static String getPreceedingExtensionFromURL(URL url)
	{
		return getPreceedingExtensionFrom(url.getPath());
	}
	public static String createLocalFileStringFromURL(URL url, boolean stayLocal)
	{
		String suggestedPath = "/";
		
		if (url!=null)
		{
			if (url.getProtocol().equalsIgnoreCase("file"))
			{
				try
				{
					suggestedPath = new File(url.toURI()).toString();
				}
				catch (URISyntaxException ex)
				{
					Log.error("Helpers::createLocalFileStringFromURL", ex);
				}
			}
			else
			{
				if (!stayLocal)
					suggestedPath = createStringFomURL(url);
				else
				{
					try
					{
						suggestedPath = HOMEDIR + getFileNameFromURL(url);
					}
					catch (SecurityException ex)
					{
						Log.error("Helpers::createLocalFileStringFromURL", ex);
					}
				}
			}
		}
		return suggestedPath;
	}
	public static boolean urlExists(URL url)
	{
		if (url==null) return false;
		
		if (url.getProtocol().equalsIgnoreCase("file"))
		{
			try
			{
				File f = new File(url.toURI());
				return f.exists();
			}
			catch (Throwable ex)
			{
			}
			return false;
		}
		try
		{
			InputStream in = null;
			try 
			{ 
				in = url.openStream(); 
			} 
			catch (Throwable ex) 
			{ 
				return false;
			}
			finally
			{
				if (in!=null) try { in.close(); } catch (Throwable e) { /*NOOP */}
			}
			return true;
		}
		catch (Throwable ex)
		{
		}
		return false;
	}
        
	/**
	 * Checks first the File exists method - will try URL than
	 * @since 01.05.2013
	 * @param url
	 * @return
	 */
	public static boolean urlExists(String url)
	{
		return urlExists(Helpers.createURLfromString(url));
	}
	/**
	 * If the baseURL provided is not absolut this method will generate an absolute file path
	 * based on the inputFileName string
	 * @param baseURL
	 * @param fileName
	 * @return
	 * @since 23.03.2013
	 */
        public static String getFileName(String audiofile)
        {
            StringTokenizer stre1 = new StringTokenizer(audiofile,"//\\");
                                 String temp1="";
                                 while(stre1.hasMoreTokens())
                                 {
                                      temp1=stre1.nextToken();
                                 }
                                 return temp1;
        }
	public static URL createAbsolutePathForFile(final URL baseURL, final String inputFileName)
	{
		String fileName = inputFileName;
		try
		{
			if (!Helpers.urlExists(fileName))
			{
				if (File.separatorChar != '/') fileName = fileName.replace(File.separatorChar, '/');

				// Create a URL object to the file
				String path = Helpers.createStringFomURL(baseURL);
				
				// get rid of playlist file name
				int lastSlash = path.lastIndexOf('/');
				StringBuilder relPath = new StringBuilder(path.substring(0, lastSlash + 1));
				
				if (fileName.startsWith("/")) fileName = fileName.substring(1);
				int iterations = 0;
				URL fullURL = Helpers.createURLfromString(((new StringBuilder(relPath)).append(fileName)).toString());
				while (fullURL!=null && !urlExists(fullURL) && iterations<256)
				{
					relPath.append("../");
					fullURL = Helpers.createURLfromString(((new StringBuilder(relPath)).append(fileName)).toString());
					iterations++;
				}
				if (iterations<256 && fullURL!=null)
				{
					try
					{
						return (fullURL.toURI().normalize()).toURL();
					}
					catch (URISyntaxException x)
					{
						Log.error("[createAbsolutePathForFile]", x);
					}
					return fullURL;
				}
				else
				{
					Log.info("File not found: " + inputFileName + " in relation to " + baseURL);
					return Helpers.createURLfromString(inputFileName);
				}
			}
			else
				return createURLfromString(fileName);
		}
		catch (Throwable ex)
		{
			Log.error("createAbsolutePathForFile", ex);
		}
		Log.info("Illegal filename specification: " + inputFileName + " in playlist " + baseURL);
		return null;
	}
	/**
	 * Retrieves a java.io.File object via FileChooser
	 * @since 01.06.2013
	 * @param parent can be null. If not, the filechooser is centered to the component
	 * @param showDir can be null. Is the start directory to begin the search or a preselected file
	 * @param action a String for the "open File"-Button
	 * @param filter a FileChooserFilter
	 * @param type 0=load-Mod 1=save-mode
	 * @param multiFileSelection true: multiple Files can be selected
	 * @return
	 * @since 23.03.2013
	 */
        
	public static FileChooserResult selectFileNameFor(final java.awt.Component parent, final String showDir, final String action, final javax.swing.filechooser.FileFilter[] filter, final int type, final boolean multiFileSelection)
	{
		String dir = (showDir==null)?HOMEDIR:showDir;
		// Try to work with URL - map "dir" it to a local File
		try
		{
			final File f = new File(dir);
			dir = f.getCanonicalPath();
		}
		catch (Exception ex)
		{
			Log.error("Helpers::selectFileNameFor", ex);
		}
		
		final File theFile = new File(dir);
		File theDirectory = new File(dir);
	    while (theDirectory!=null && (!theDirectory.isDirectory() || !theDirectory.exists()))
	    {
	    	theDirectory = theDirectory.getParentFile();
	    }
	    final javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(theDirectory);
	    if (filter!=null)
	    {
	    	for (int i=filter.length-1; i>=0; i--) // count downwards 'cause the last one is the default
	    		chooser.addChoosableFileFilter(filter[i]);
	    }
	    if (!theFile.isDirectory()) chooser.setSelectedFile(theFile);
	    chooser.setApproveButtonText(action);
	    chooser.setMultiSelectionEnabled(multiFileSelection);
	    final int result = (type==0)?chooser.showOpenDialog(parent):chooser.showSaveDialog(parent);

	    if (result==javax.swing.JFileChooser.APPROVE_OPTION)
	    {
	    	File [] selectedFiles = (multiFileSelection)?chooser.getSelectedFiles(): new File[] { chooser.getSelectedFile() };
	    	return new FileChooserResult(chooser.getFileFilter(), selectedFiles);
	    }
	    else
	    	return null;
	}
	/**
	 * @since 05.01.2013
	 * @param location
	 * @return a string representing the point class
	 */
	public static String getStringFromPoint(final java.awt.Point location)
	{
		return ((new StringBuilder()).append((int)location.getX()).append('x').append((int)location.getY())).toString();
	}
	/**
	 * @since 05.01.2013
	 * @param point
	 * @return a Point class from the string
	 */
	public static java.awt.Point getPointFromString(final String point)
	{
		final int xIndex = point.indexOf('x');
		String x = point.substring(0, xIndex);
		String y = point.substring(xIndex+1);
		return new java.awt.Point(Integer.parseInt(x), Integer.parseInt(y));
	}
	/**
	 * @since 05.01.2013
	 * @param location
	 * @return a string representing the point class
	 */
	public static String getStringFromDimension(final java.awt.Dimension dimension)
	{
		return ((new StringBuilder()).append((int)dimension.getWidth()).append('x').append((int)dimension.getHeight())).toString();
	}
	/**
	 * @since 05.01.2013
	 * @param point
	 * @return a Point class from the string
	 */
	public static java.awt.Dimension getDimensionFromString(final String dimension)
	{
		final int xIndex = dimension.indexOf('x');
		String width = dimension.substring(0, xIndex);
		String height = dimension.substring(xIndex+1);
		return new java.awt.Dimension(Integer.parseInt(width), Integer.parseInt(height));
	}
	/**
	 * @since 12.07.2013
	 * @param insets
	 * @return
	 */
	public static java.awt.Insets getInsetsFromString(final String insets)
	{
		final StringTokenizer tok = new StringTokenizer(insets, ",");
		int left = Integer.parseInt(tok.nextToken().trim());
		int top = Integer.parseInt(tok.nextToken().trim());
		int right = Integer.parseInt(tok.nextToken().trim());
		int bottom = Integer.parseInt(tok.nextToken().trim());
		return new Insets(top, left, bottom, right);
	}
	public static java.awt.Color getColorFromString(final String color)
	{
		final StringTokenizer tok = new StringTokenizer(color, ",");
		int r = Integer.parseInt(tok.nextToken().trim());
		int g = Integer.parseInt(tok.nextToken().trim());
		int b = Integer.parseInt(tok.nextToken().trim());
		return new Color(r,g,b);
	}
	/**
	 * @since 06.02.2013
	 * @param millis
	 * @return
	 */
        
	public static String getTimeStringFromMilliseconds(long millis)
	{
		//int sec = (int)((millis/1000L)%60L);
		//int min = (int)(millis/60000L);
                
                return String.format("%.3f",millis/1000.0);
		//return ((min<10)?"  ":"") + Integer.toString(min) + ':' + ((sec<10)?"0":"") + Integer.toString(sec)+":"+((millis+"000").substring(2,4))+"["+millis/1000.0+"]";
	}
	/**
	 * Convert from decimalValue to DeciBel
	 * @since 14.01.2013
	 * @param dbValue
	 * @return
	 */
	public static double getDecimalValueFrom(float dbValue)
	{
		return Math.pow(10, dbValue / 20.0);
	}
	/**
	 * convert from DeciBel to decimalValue
	 * @since 14.01.2013
	 * @param decimalValue
	 * @return
	 */
	public static double getDBValueFrom(final float decimalValue)
	{
		return Math.log10(decimalValue)*20.0f;
	}
	/**
	 * @since 03.04.2013
	 * @param timeString
	 * @return
	 */
	public static long getMillisecondsFromTimeString(String timeString)
	{
		int minIndex = timeString.indexOf(':');
		int min = Integer.parseInt(timeString.substring(0, minIndex).trim());
		String secString = timeString.substring(minIndex + 1);
		int secIndex = secString.indexOf(':');
		if (secIndex == -1) secIndex = secString.length();
		int sec = Integer.parseInt(secString.substring(0, secIndex).trim());
		return (min*60+sec) * 1000L;
	}
	/**
	 * Prints the info about all installed and available audio lines
	 * @since 09.06.2013
	 */
	public static String getAudioInfos()
	{
		final StringBuilder result = (new StringBuilder("Running on ")).append(System.getProperty("os.arch"));
		result.append("\nMixerInfo:\n");
		final Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
		for (int i = 0; i < mixerInfo.length; i++)
		{
			result.append(mixerInfo[i]).append('\n');
			Mixer mixer = AudioSystem.getMixer(mixerInfo[i]);
			Line.Info[] targetLineInfos = mixer.getTargetLineInfo();
			for (int j = 0; j < targetLineInfos.length; j++)
			{
				result.append("Targetline(").append(j).append("): ").append(targetLineInfos[j]).append('\n');
				if (targetLineInfos[j] instanceof DataLine.Info)
				{
					AudioFormat audioFormats[] = ((DataLine.Info) targetLineInfos[j]).getFormats();
					for (int u = 0; u < audioFormats.length; u++)
					{
						result.append("Audioformat(").append(u).append("): ").append(audioFormats[u]).append('\n');
//						System.out.print("Checking..");
//						DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormats[u]);
//						if (!AudioSystem.isLineSupported(info))
//						{
//							System.out.println("It is NOT supported.");
//						}
//						else
//						{
//							System.out.println("It is supported.");
//						}
					}
				}
			}
			result.append("---------------------------------------------------------------------\n");
		}
		return result.toString();
	}
	/**
	 * Registers all Classes that should not load during playback
	 * @since 26.12.2007
	 * @throws ClassNotFoundException
	 */
	public static void registerAllClasses() throws ClassNotFoundException
	{
		Class.forName("sm.smcreator.smc.system.Log");
		Class.forName("sm.smcreator.smc.system.Helpers");
		
		// Interpolation Routines - doing pre-calculations
		
		// The following are essential for registration at the ModuleFactory

		// The following are essential for registration at the MultimediaContainerManager
		Class.forName("sm.smcreator.smc.multimedia.wav.WavContainer");
		Class.forName("sm.smcreator.smc.multimedia.mp3.MP3Container");
		Class.forName("sm.smcreator.smc.multimedia.ogg.OGGContainer");
	}
	/**
	 * Opens a txt-File on the server containing the current populated
	 * Version. 
	 * Compare to Helpers.VERSION
	 * @since 19.10.2013
	 * @return Null, if check fails, or the current Version populated
	 */
        
	public static String doOpenFile2(String s1,String s2)
	{
            final JFileChooser fc = new JFileChooser();
            fc.setDialogTitle(s1);
            fc.setApproveButtonText(s2);
            int returnVal=fc.showOpenDialog(null);
            String str=null;
             if (returnVal == JFileChooser.APPROVE_OPTION)
             { 
             str = fc.getSelectedFile().toString();
             JOptionPane.showMessageDialog(null, str);
             }
             else
                 JOptionPane.showMessageDialog(null,"Error in this Operation");  
           return str;             
	}
	public static String getCurrentServerVersion()
	{
		BufferedReader reader = null;
		try
		{
			URL version_url = new URL(VERSION_URL);
			reader = new BufferedReader(new InputStreamReader(version_url.openStream()));
			String version = reader.readLine();
			reader.close();
			reader = null;
			return version;
		}
		catch (Throwable ex)
		{
			Log.error("CheckForUpdate failed", ex);
		}
		finally
		{
			if (reader!=null) try { reader.close(); } catch (Exception ex) { Log.error("IGNORED", ex); }
		}
		return null;
	}
	/**
	 * Compares two version strings
	 * @since 16.01.2013
	 * @param v1
	 * @param v2
	 * @return v1>v2: 1; v1==v2: 0; v1<v2: -1
	 */
	public static int compareVersions(String v1, String v2)
	{
		if (v1.startsWith("V")) v1 = v1.substring(1);
		StringTokenizer t1 = new StringTokenizer(v1, ".");
		if (v2.startsWith("V")) v2 = v2.substring(1);
		StringTokenizer t2 = new StringTokenizer(v2, ".");
		while (t1.hasMoreTokens() && t2.hasMoreTokens())
		{
			int subV1 = Integer.parseInt(t1.nextToken());
			int subV2 = Integer.parseInt(t2.nextToken());
			if (subV1 < subV2) return -1;
			if (subV1 > subV2) return 1;
		}
		if (t1.hasMoreTokens() && !t2.hasMoreTokens()) return 1;
		if (!t1.hasMoreTokens() && t2.hasMoreTokens()) return -1;
		return 0;
	}
	/**
	 * @since 19.10.2013
	 * @param destination
	 * @param bar A JProgressBar or null
	 * @return
	 */
}
