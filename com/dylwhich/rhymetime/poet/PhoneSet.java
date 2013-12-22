package com.dylwhich.rhymetime.poet;

import com.dylwhich.rhymetime.poet.WordDatabase.PhoneType;

public class PhoneSet {
	public static final PhoneSet[] PHONE_SET = new PhoneSet[] { new PhoneSet(PhoneType.a_in_dab, "ae", true),
			new PhoneSet(PhoneType.a_in_air, "ey", true), new PhoneSet(PhoneType.a_in_far, "ao", true),
			new PhoneSet(PhoneType.a_in_day, "ay", true), new PhoneSet(PhoneType.a_in_ado, "ah", true),
			new PhoneSet(PhoneType.ir_in_tire, "ire", true), new PhoneSet(PhoneType.b_in_nab, "b", false),
			new PhoneSet(PhoneType.ch_in_ouch, "ch", false), new PhoneSet(PhoneType.d_in_pod, "d", false),
			new PhoneSet(PhoneType.e_in_red, "e", true), new PhoneSet(PhoneType.e_in_see, "ee", true),
			new PhoneSet(PhoneType.f_in_elf, "f", false), new PhoneSet(PhoneType.g_in_fig, "g", false),
			new PhoneSet(PhoneType.h_in_had, "h", false), new PhoneSet(PhoneType.w_in_white, "wh", false),
			new PhoneSet(PhoneType.i_in_hid, "i", true), new PhoneSet(PhoneType.i_in_ice, "eye", true),
			new PhoneSet(PhoneType.g_in_vegetably, "g", false), new PhoneSet(PhoneType.c_in_act, "k", false),
			new PhoneSet(PhoneType.l_in_ail, "l", false), new PhoneSet(PhoneType.m_in_aim, "m", false),
			new PhoneSet(PhoneType.ng_in_bang, "ng", false), new PhoneSet(PhoneType.n_in_and, "n", false),
			new PhoneSet(PhoneType.oi_in_oil, "oy", true), new PhoneSet(PhoneType.o_in_bob, "aa", true),
			new PhoneSet(PhoneType.ow_in_how, "ow", true), new PhoneSet(PhoneType.o_in_dog, "ah", true),
			new PhoneSet(PhoneType.o_in_boat, "oh", true), new PhoneSet(PhoneType.oo_in_too, "oo", true),
			new PhoneSet(PhoneType.oo_in_book, "ooh", true), new PhoneSet(PhoneType.p_in_imp, "p", false),
			new PhoneSet(PhoneType.r_in_ire, "er", false), new PhoneSet(PhoneType.sh_in_she, "sh", false),
			new PhoneSet(PhoneType.s_in_sip, "s", false), new PhoneSet(PhoneType.th_in_bath, "dth", false),
			new PhoneSet(PhoneType.th_in_the, "th", false), new PhoneSet(PhoneType.t_in_tap, "t", false),
			new PhoneSet(PhoneType.u_in_cup, "uh", true), new PhoneSet(PhoneType.u_in_burn, "u", true),
			new PhoneSet(PhoneType.v_in_average, "v", false), new PhoneSet(PhoneType.w_in_win, "w", false),
			new PhoneSet(PhoneType.y_in_you, "y", false), new PhoneSet(PhoneType.s_in_vision, "zh", false),
			new PhoneSet(PhoneType.z_in_zoo, "z", false), new PhoneSet(PhoneType.a_in_ami, "a", true),
			new PhoneSet(PhoneType.n_in_francoise, "n", false), new PhoneSet(PhoneType.r_in_der, "r", false),
			new PhoneSet(PhoneType.ch_in_bach, "chh", false), new PhoneSet(PhoneType.eu_in_bleu, "eu", true),
			new PhoneSet(PhoneType.u_in_duboise, "u", true), new PhoneSet(PhoneType.wa_in_noire, "WA", true) };

	private PhoneSet(PhoneType t, String display, boolean isSyllable) {
		id = t;
		this.display = display;
		this.isSyllable = isSyllable;
	}

	public PhoneType id;
	public String display;
	public boolean isSyllable;
};