// XORCracker.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>
#include <fstream>

using namespace std;

int _tmain(int argc, _TCHAR* argv[])
{

	ofstream file;
	file.open("output.txt");

	char upper[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ', ',', '\"' };
	char lower[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ' ', ',', '\"' };
	char punct[] = { ' ', ',', '\"', '.' };

	int cipher1[] = { 0x03, 0x16, 0x4A, 0x4B, 0x17, 0xB7, 0xAB, 0x69, 0x0C, 0x4F, 0x42, 0xEF, 0xB8, 0xF1, 0xBC, 0x2C, 0xAC, 0xF8, 0xAB, 0x7D, 0xB5, 0x0D, 0xC0, 0xB4, 0x6D, 0xA8 };
	int cipher2[] = { 0x00, 0x1F, 0x59, 0x4F, 0x16, 0xB3, 0xE7, 0x2D, 0x00, 0x0E, 0x42, 0xEF, 0xA2, 0xA2, 0xFA, 0x2E, 0xA6, 0xE8, 0xAB, 0x79, 0xAE, 0x0E, 0xDA, 0xB4, 0x70, 0xA8 };
	int cipher3[] = { 0x03, 0x1F, 0x4E, 0x49, 0x01, 0xF2, 0xFF, 0x7F, 0x06, 0x0E, 0x58, 0xF9, 0xEC, 0xA6, 0xBF, 0x27, 0xBD, 0xBC, 0xE6, 0x63, 0xA9, 0x12, 0xDF, 0xBF, 0x79, 0xA8 };
	int cipher4[] = { 0x03, 0x13, 0x55, 0x50, 0x05, 0xA1, 0xAB, 0x6C, 0x11, 0x0A, 0x0C, 0xEE, 0xA3, 0xA5, 0xFA, 0x38, 0xBC, 0xF5, 0xFF, 0x6F, 0xFA, 0x06, 0xD9, 0xBE, 0x7A, 0xA8 };
	int cipher5[] = { 0x1F, 0x13, 0x4A, 0x59, 0x44, 0xB1, 0xEA, 0x63, 0x43, 0x01, 0x43, 0xF4, 0xEC, 0xB3, 0xBF, 0x69, 0xBD, 0xF3, 0xE7, 0x6F, 0xA8, 0x00, 0xC2, 0xB4, 0x7A, 0xA8 };
	int cipher6[] = { 0x07, 0x12, 0x4A, 0x0A, 0x06, 0xBD, 0xF3, 0x2D, 0x14, 0x0E, 0x5F, 0xA0, 0xBF, 0xB9, 0xB3, 0x39, 0xB9, 0xF9, 0xEF, 0x2A, 0xAE, 0x0E, 0xD2, 0xB0, 0x67, 0xA8 };
	int cipher7[] = { 0x1D, 0x1F, 0x58, 0x0A, 0x10, 0xBD, 0xF2, 0x7E, 0x43, 0x0B, 0x43, 0xA0, 0xA2, 0xBE, 0xAE, 0x69, 0xA5, 0xFD, 0xF8, 0x7E, 0xFA, 0x0D, 0xD9, 0xBF, 0x79, 0xA8 };

	
	int col = 0;
	cin >> col;
	file << "Col : " << col << endl;

	int key = 0;
	// loop that brutes
	
	for (int i = 0; i < 30; i++)// loop the ASCII
	{

			// int key = cipher ^ (char)'A'	
			if (col == 0)
			{
				key = (static_cast<char>(cipher1[col])) ^ upper[i];
				file << "===================" << endl;
				file << "Guess : " << upper[i] << endl;
				file << "Key   : " << (static_cast<char>(key)) << endl;
			}

			if (col >= 1 && col < 25)
			{
				key = (static_cast<char>(cipher1[col])) ^ lower[i];
				file << "===================" << endl;
				file << "Guess : " << lower[i] << endl;
				file << "Key   : " << (static_cast<char>(key)) << endl;
			}

			if (col == 25)
			{
				if (i == 4)
				{
					cout << "Done." << endl;
					cin >> col;
					exit(EXIT_SUCCESS);
				}

				key = (static_cast<char>(cipher1[col])) ^ punct[i];
				file << "===================" << endl;
				file << "Guess : " << punct[i] << endl;
				file << "Key   : " << (static_cast<char>(key)) << endl;
			}
			// char decodedChar = cipherChar ^ key
			int fin1 = cipher1[col] ^ key;
			int fin2 = cipher2[col] ^ key;
			int fin3 = cipher3[col] ^ key;
			int fin4 = cipher4[col] ^ key;
			int fin5 = cipher5[col] ^ key;
			int fin6 = cipher6[col] ^ key;
			int fin7 = cipher7[col] ^ key;

			file << "-------------------" << endl;
			file << hex << cipher1[col] << "  : " << (static_cast<char>(fin1)) << endl;
			file << hex << cipher2[col] << "  : " << (static_cast<char>(fin2)) << endl;
			file << hex << cipher3[col] << "  : " << (static_cast<char>(fin3)) << endl;
			file << hex << cipher4[col] << "  : " << (static_cast<char>(fin4)) << endl;
			file << hex << cipher5[col] << "  : " << (static_cast<char>(fin5)) << endl;
			file << hex << cipher6[col] << "  : " << (static_cast<char>(fin6)) << endl;
			file << hex << cipher7[col] << "  : " << (static_cast<char>(fin7)) << endl;
			file << "===================" << endl;
			file << endl;
		}
	//
}