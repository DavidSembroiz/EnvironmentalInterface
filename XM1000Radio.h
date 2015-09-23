#ifndef XM1000RADIO_H
#define XM1000RADIO_H

enum {
	AM_XM1000MSG = 7
};

typedef nx_struct XM1000Msg {
	nx_uint32_t nodeid;
	nx_uint32_t temp;
	nx_uint32_t hum;
	nx_uint32_t light;
	nx_uint32_t batt;
} XM1000Msg;

#endif
