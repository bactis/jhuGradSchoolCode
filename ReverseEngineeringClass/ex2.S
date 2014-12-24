BITS 32

_start:

	call	$+5
	push	ebp			; save ebp
	mov	ebp, esp		; update ebp with the stack pointer

	push	edi			; save edi
	push	esi			; save esi
	push	ebx			; save ebx
	push	edx			; save ebx
	push	ecx			; save ecx
	push	eax

	call	pp
pp:
	pop	edx

	lea	esi, [ enc_string1 ]
	add	esi, edx
	add	esi, (_start - pp )

	lea	ecx, [ value ]
	add	ecx, edx
	add	ecx, (_start - pp )
	mov	[ ecx ], esi


	mov	ecx, ( enc_string1_end - enc_string1 )
	push	ecx

	lea	ecx, [ value ]
	add	ecx, edx
	add	ecx, (_start - pp )
	push	dword [ ecx ]

	lea	ecx, [ dec_string1 ]
	add	ecx, edx
	add	ecx, (_start - pp )
	push	ecx 
	call	decode_it


	mov	ecx, ( dec_string1_end - dec_string1 )
	push	ecx

	lea	ecx, [ dec_string1 ]
	add	ecx, edx
	add	ecx, (_start - pp )
	push	ecx 
	call	print_it
	pop	edx
	pop	edx


	pop	eax
	pop	ecx			; restore ecx
	pop	edx			; restore edx
	pop	ebx			; restore ebx
	pop	esi			; restore esi
	pop	edi			; restore edi

	pop	ebp			; restore ebp
	retn	0			; this func took 0 arguments


print_it:
	push	ebp
	mov	ebp, esp

	push	eax
	push	ebx
	push	ecx
	push	edx

	mov eax,4            ; 'write' system call
	mov ebx,1            ; file descriptor 1 = screen
	mov ecx, [ ebp + 08h ]        ; string to write
	mov edx, [ ebp + 0ch ]     ; length of string to write
	;int 80h              ; call the kernel

	pop	edx
	pop	ecx
	pop	ebx
	pop	eax

	pop	ebp
	retn

decode_it:
	push	ebp
	mov	ebp, esp

	push	edi
	push	esi
	push	ebx
	push	edx

	mov	ecx, dword [ ebp + 10h ]
	mov	esi, dword [ ebp + 0ch ]
	mov	edi, dword [ ebp + 08h ]

	xor	edx, edx	
loop_:
	mov	eax, [ esi ]
	mov	ebx, 0x00000041
	xor	eax, ebx
	mov	[ edi ], eax
	inc	esi
	inc	edi
	inc	edx
	cmp	ecx, edx
	jnz	loop_


	pop	edx
	pop	ebx
	pop	esi
	pop	edi

	pop	ebp
	retn	0ch


enc_string1: db 0x09, 0x24, 0x2d, 0x2d, 0x2e, 0x41
enc_string1_end:

dec_string1: dd 0,0,0,0,0,0,0
dec_string1_end:

value:	dd 0
