#include "Windows.h"
#include "MathLibrary.h"
namespace MathLibrary
{
	double Functions::Add(double a, double b)
	{
		return a + b;
	}
	double Functions::Multiply(double a, double b)
	{
		return a * b;
	}
	double Functions::AddMultiply(double a, double b)
	{
		return a + (a * b);
	}
}