
function assertEquals(a, b)
{
	if (a != b)
	{
		throw new Error("objects are different.  Expected:(" + a + ") Actual:(" + b + ")");
	}
}

function assertNotNull(obj)
{
	if (obj == null)
	{
		throw new Error("object expected to be not null");
	}
}

function assertTrue(b)
{
	if (!b)
	{
		throw new Error("False detected while expecting true");
	}
}

function assertFalse(b)
{
	if (b)
	{
		throw new Error("True detected while epxecting false");
	}
}

